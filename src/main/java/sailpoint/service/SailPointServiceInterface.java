package sailpoint.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import sailpoint.object.Session;
import sailpoint.object.Source;

import java.util.List;

import static sailpoint.service.SailPointService.Constants.QUERY_COUNT;

public interface SailPointServiceInterface {

	@POST( "oauth/token" )
	Call<Session> getSession(
			@Query( "grant_type" ) String grant_type,
			@Query( "client_id" ) String client_id,
			@Query( "client_secret" ) String client_secret );

	@GET( "beta/sources/")
	Call<List<Source>> listSources(
			@Query( QUERY_COUNT  ) boolean count,
			@Query( "pageSize"  ) int pageSize,
			@Query( "offset"  ) int offset,
			@Query( "filters"  ) String filters,
			@Query( "sorters"  ) String sorters );

	@GET( "beta/sources/{id}")
	Call<Source> getSource(
			@Path( "id" ) String id );

	@Multipart
	@POST( "beta/sources/{id}/load-accounts")
	Call<ResponseBody> aggregateAccounts(
			@Path( "id" ) String id,
			@Part( "disableOptimization") RequestBody disableOptimization );

	@Multipart
	@POST( "beta/sources/{id}/load-accounts")
	Call<ResponseBody> aggregateAccounts(
			@Path( "id" ) String id,
			@Part( "disableOptimization") RequestBody disableOptimization,
			@Part( "file\"; filename=\"file.csv\"") RequestBody file );

	@Multipart
	@POST( "beta/sources/{id}/load-entitlements")
	Call<ResponseBody> aggregateEntitlements(
			@Path( "id" ) String id,
			@Part("objectType") RequestBody entitlementType,
			@Part( "file\"; filename=\"file.csv\" " ) RequestBody file );
}
