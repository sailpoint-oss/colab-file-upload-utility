package sailpoint.identitynow.api.services;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import sailpoint.identitynow.api.object.Session;

public interface AuthorizationService {
	
	@POST( "oauth/token" )
	Call<Session> getSession( 
		@Query( "grant_type" ) String grant_type,
		@Query( "client_id" ) String client_id,
		@Query( "client_secret" ) String client_secret );

}
