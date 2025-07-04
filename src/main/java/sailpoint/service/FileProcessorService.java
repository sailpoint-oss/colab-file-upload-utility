package sailpoint.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sailpoint.app.FileUploadUtility;
import sailpoint.object.Source;
import sailpoint.object.config.Config;
import sailpoint.object.config.ConfigAggregation;
import sailpoint.utils.Reporter;

public class FileProcessorService {
	private Config config;

	// 02/07/2025: Added Log4J
	private static Logger LOG = LogManager.getLogger(FileProcessorService.class);

	/*
	 * This sourceReferenceMap is used to map old source IDs to new source IDs.
	 * Keyed by old source IDs; Values are new source IDs 184744 :
	 * 2c918087701c40cf01701dfdf2c61e2a
	 */
	private Map<String, String> sourceReferenceMap = null;

	private SailPointService sailPointService;

	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");

	public FileProcessorService(Config config, SailPointService sailPointService) {
		this.config = config;
		this.sailPointService = sailPointService;
	}

	public void processFiles(List<File> files, ConfigAggregation configAggregation, Reporter reporter) {
		if (CollectionUtils.isNotEmpty(files)) {
			for (File file : files) {

				if (file.isDirectory()) {

					LOG.info("Analyzing directory: " + file);

					@SuppressWarnings("unchecked")
					Iterator<File> fileIterator = FileUtils.iterateFiles(file, configAggregation.getExtensions(),
							configAggregation.isRecursive());

					while (fileIterator.hasNext()) {
						processFile(fileIterator.next(), configAggregation, reporter);
					}

				} else {
					LOG.info("Analyzing " + file.getName() + " file: " + file);
					processFile(file, configAggregation, reporter);
				}
			}
		}
	}

	public void processAggregations(Config config, Reporter reporter) {
		LOG.debug("processAggregations: entry");

		ConfigAggregation[] aggregations = config.getAggregations();

		for (ConfigAggregation agg : aggregations) {
			File inDirectory = new File(agg.getStructure().getIn());

			// TODO: Check required fields.

			if (!inDirectory.isDirectory()) {
				processFile(inDirectory, agg, reporter);
			} else {
				LOG.info("Analyzing directory: " + inDirectory);

				@SuppressWarnings("unchecked")
				Iterator<File> fileIterator = FileUtils.iterateFiles(inDirectory, agg.getExtensions(),
						agg.isRecursive());

				while (fileIterator.hasNext()) {
					processFile(fileIterator.next(), agg, reporter);
				}
			}

		}
	}

	private void processFile(File file, ConfigAggregation aggregation, Reporter reporter) {
		LOG.info("Analyzing " + aggregation.getObjectType() + " file: " + file.getName());

		if (aggregation.isSimulate()) {

			LOG.info("\tFile [" + file.getName() + "]: Has not been processed due to simulation. Skipping...");
			reporter.skipped(file.getAbsolutePath());

		} else if (StringUtils.length(aggregation.getSourceId()) == 32) {

			ResponseBody response = null;

			try {
				LOG.info(String.format("%1$-20s %2$-30s ", " ObjectType:", aggregation.getObjectType()));

				if (FileUploadUtility.DEFAULT_ACCOUNT_AGGREGATION.equals(aggregation.getObjectType()))
					LOG.info(
							String.format("%1$-20s %2$-30s ", " Optimization:", !aggregation.isDisableOptimization()));

				LOG.info(String.format("%1$-20s %2$-30s ", " Recursive:", aggregation.isRecursive()));
				LOG.info(String.format("%1$-20s %2$-30s ", " Extensions:",
						StringUtils.join(aggregation.getExtensions(), ",")));
				LOG.info(String.format("%1$-20s %2$-30s ", " Simulation:", aggregation.isSimulate()));

				LOG.info(String.format("%1$-20s %2$-30s ", " Timeout:", aggregation.getTimeout()));
				LOG.info(
						"------------------------------------------------------------------------------------------------------------");

				if (aggregation.isEnableFileJourney()) {
					File newFile = new File(aggregation.getStructure().getStage() + File.separator
							+ formatter.format(new java.util.Date()) + "-" + file.getName());
					file.renameTo(newFile);
					file = newFile;
				}

				/*
				 * If the objectType is 'account' then we'll aggregate it as an account. This is
				 * the default behavior.
				 */
				if ("account".equalsIgnoreCase(aggregation.getObjectType())) {

					LOG.debug("\tFile [" + file.getName() + "]: Submitting Account Aggregation: Source ID["
							+ aggregation.getSourceId() + "], Disable Optimization["
							+ aggregation.isDisableOptimization() + "]");
					response = sailPointService.aggregateAccounts(aggregation.getSourceId(),
							aggregation.isDisableOptimization(), file);

					/*
					 * If the objectType is something else then we'll aggregate it as an
					 * entitlement. Entitlement aggregations can be many different objectTypes.
					 */
				} else {

					LOG.debug("\tFile [" + file.getName() + "]: Submitting Entitlement Aggregation: Source ID["
							+ aggregation.getSourceId() + "], Object Type[" + aggregation.getObjectType() + "]");
					response = sailPointService.aggregateEntitlements(aggregation.getSourceId(),
							aggregation.getObjectType(), file);

				}

				LOG.debug("\tFile [" + file.getName() + "]: Aggregation response: " + response.string());

				LOG.info("\tFile [" + file.getName() + "]: Aggregated successfully.");
				if (aggregation.isEnableFileJourney()) {
					File newFile = new File(aggregation.getStructure().getArchive() + File.separator + file.getName());
					file.renameTo(newFile);
					file = newFile;
				}
				reporter.success(file.getAbsolutePath());

			} catch (Exception e) {

				LOG.info("\tFile [" + file.getName() + "]: Error: " + e.getMessage());
				if (aggregation.isEnableFileJourney()) {
					File newFile = new File(aggregation.getStructure().getError() + File.separator + file.getName());
					file.renameTo(newFile);
					file = newFile;
				}
				reporter.error(file.getAbsolutePath());

			}

		} else {

			LOG.info("\tFile [" + file.getName() + "]: Does not contain a valid source ID. Skipping...");
			reporter.skipped(file.getAbsolutePath());
		}
	}

	private Map<String, String> buildSourceReferenceMap() {

		Map<String, String> sourceReferenceMap = new HashMap<String, String>();

		LOG.debug(
				"------------------------------------------------------------------------------------------------------------");
		LOG.debug(" Building source file lookup table. Starting source iteration. ");
		LOG.debug(" To avoid this scan, please switch to new Source IDs in your file names.");
		LOG.debug(" Older Source ID references will not be used in the future.");
		LOG.debug(
				"------------------------------------------------------------------------------------------------------------");

		Iterator<Source> it = sailPointService.listSources();

		/*
		 * Iterate through all the sources in the system. For customers with a lot of
		 * sources, this lookup could be somewhat painful. To avoid these kinds of
		 * lookups, we should move away from old CC IDs, and move to modern Source IDs.
		 * :)
		 */
		while (it.hasNext()) {

			Source source = it.next();
			String oldSourceReference = (String) source.getConnectorAttribute("cloudExternalId");
			String newSourceReference = source.getId();

			/*
			 * This sourceReferenceMap is used to map old source IDs to new source IDs.
			 * Keyed by old source IDs; Values are new source IDs 184744 :
			 * 2c918087701c40cf01701dfdf2c61e2a
			 */
			if (oldSourceReference != null && newSourceReference != null) {
				LOG.debug(String.format("%1$-10s %2$-40s ", " " + oldSourceReference, " : " + newSourceReference));
				sourceReferenceMap.put(oldSourceReference, newSourceReference);
			}
		}

		LOG.debug(
				"------------------------------------------------------------------------------------------------------------");

		return sourceReferenceMap;

	}

	private String getSourceReferenceFromFile(File file) {

		/*
		 * First, look for the new-form Source ID in the file name This can be found on
		 * the Source object as "id": "2c918087701c40cf01701dfdf2c61e2a" and consists of
		 * 32 characters of any 0-9, a-f
		 *
		 * We'll parse the file name, and extract the new-form Source ID e.g.,
		 * 2c918087701c40cf01701dfdf2c61e2a - Something.csv Would return
		 * "2c918087701c40cf01701dfdf2c61e2a"
		 */
		Matcher newMatcher = Pattern.compile("^(\\s)?([0-9a-f]{32})").matcher(file.getName());

		if (newMatcher.find()) {

			String fileSourceId = StringUtils.trim(newMatcher.group());

			LOG.debug("\tFile [" + file.getName() + "]: detected with source ID reference [" + fileSourceId + "].");

			return fileSourceId;

		}

		/*
		 * Second, look for the old-form Source ID in the file name This is mainly there
		 * for backwards compatibility purposes.
		 *
		 * This can be found on the Source object under "connectorAttributes" as
		 * "cloudExternalId": "184744" and consists of up to 10 characters of any number
		 * (0-9)
		 *
		 * We'll parse the file name, and extract the old-form Source ID e.g., 184744 -
		 * Something.csv Would return "184744"
		 */
		Matcher oldMatcher = Pattern.compile("^(\\s)?([0-9]{4,10})").matcher(file.getName());

		if (oldMatcher.find()) {

			String fileSourceId = StringUtils.trim(oldMatcher.group());

			LOG.debug("\tFile [" + file.getName() + "]: Detected with older source ID reference [" + fileSourceId
					+ "]. Attempting to resolve.");

			/*
			 * First, check to see if we have a sourceReferenceMap defined. If it is null,
			 * it hasn't yet been initialized, so we enter here.
			 */
			if (this.sourceReferenceMap == null) {
				this.sourceReferenceMap = buildSourceReferenceMap();
			}

			/*
			 * At this point, the sourceReferenceMap is not null, and has already been
			 * initialized. Let's query what we have to see if we can resolve the
			 * oldSourceReference to the newSourceReference.
			 */

			if (this.sourceReferenceMap.containsKey(fileSourceId)) {

				LOG.info("\tFile [" + file.getName() + "]: Successfully resolved old source ID reference ["
						+ fileSourceId + "] to new source ID reference [" + sourceReferenceMap.get(fileSourceId) + "]");
				return sourceReferenceMap.get(fileSourceId);

			} else {

				LOG.error("\tFile [" + file.getName() + "]: Unable to resolve old source ID reference ["
						+ fileSourceId + "] to new source ID reference. This file will be skipped.");
				return null;
			}
		}

		/*
		 * This assumes we didn't find any source references in the file name.
		 */
		return null;
	}
}
