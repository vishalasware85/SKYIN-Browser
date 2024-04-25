package com.skyinbrowser.CustomJavaFiles;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDomainNameFromURL {

    private static final String hostExtractorRegexString = "(?:https?://)?(?:www\\.)?(.+\\.)(com|au\\.uk|co\\.in|be|in|uk|org\\.in|org|net|edu|gov|mil)";
    private static final Pattern hostExtractorRegexPattern = Pattern.compile(hostExtractorRegexString);

    public static String getDomainName(String url){
        if (url == null) return null;
        url = url.trim();
        Matcher m = hostExtractorRegexPattern.matcher(url);
        if(m.find() && m.groupCount() == 2) {
            return m.group(1) + m.group(2);
        }
        return null;
    }

    public static String getUrlDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        String[] domainArray = domain.split("\\.");
        if (domainArray.length == 1) {
            return domainArray[0];
        }
        return domainArray[domainArray.length - 2] + "." + domainArray[domainArray.length - 1];
    }
}
