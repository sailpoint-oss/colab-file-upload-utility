package sailpoint.utils;

import org.apache.commons.lang3.StringUtils;

public enum SailPointUrl {

    DEFAULT( ".api.identitynow.com" ),
    DEMO( ".api.identitynow-demo.com" ),
    FEDRAMP( ".api.saas.sailpointfedramp.com" );

    private final String value;

    SailPointUrl( String value ) {
        this.value = value;
    }

    /**
     * Checks to see if a candidate URL is valid against known SailPoint URL end-points.
     * @param candidateUrl
     */
    public static boolean isValid( String candidateUrl ) {

        for( SailPointUrl url : SailPointUrl.values() )
            if( StringUtils.endsWithIgnoreCase( candidateUrl, url.value ) )
                return true;

        return false;
    }

    public static String getDisplayableUrls() {

        StringBuilder sb = new StringBuilder();

        for( SailPointUrl url : SailPointUrl.values() )
            sb.append(" *" + url.value + " \n" );

        return sb.toString();
    }
}