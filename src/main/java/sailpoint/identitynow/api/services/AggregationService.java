package sailpoint.identitynow.api.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AggregationService {

	@POST( "cc/api/source/loadAccounts/{id}")
	Call<ResponseBody> aggregateAccounts(
			@Path( "id" ) String id );

	@Multipart
	@POST( "cc/api/source/loadAccounts/{id}")
	Call<ResponseBody> aggregateAccounts(
			@Path( "id" ) String id,
			@Part( "disableOptimization") RequestBody disableOptimization,
			@Part( "file\"; filename=\"file.csv\"") RequestBody file);

	@Multipart
	@POST( "cc/api/source/loadEntitlements/{id}")
	Call<ResponseBody> aggregateEntitlements(
			@Path( "id" ) String id,
			@Query("objectType") String entitlementType,
			@Part( "file\"; filename=\"file.csv\" " ) RequestBody file );
}
