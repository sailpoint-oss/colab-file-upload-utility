package sailpoint.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sailpoint.exception.FileReadException;

public class FileManagementUtils {
	// 02/07/2025: Added Log4J
	private static Logger LOG = LogManager.getLogger(FileManagementUtils.class);

	public static List<File> loadFiles(String[] files) throws FileReadException {
		LOG.debug(String.format("loadFiles: Loading %s files...", files.length));
		ArrayList<File> fileList = new ArrayList<File>();

		for (int index = 0; index < files.length; index++) {
			File file = new File(files[index]);

			LOG.debug(String.format("loadFiles: Loading %s...", file.getName()));

			if (!file.exists()) {
				throw new FileReadException(String.format("File '%s' not found.", file));
			}

			fileList.add(file);
		}
		return fileList;
	}
}
