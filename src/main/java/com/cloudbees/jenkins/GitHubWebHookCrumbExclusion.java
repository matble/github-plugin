package com.cloudbees.jenkins;

import hudson.Extension;
import hudson.security.csrf.CrumbExclusion;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Extension
public class GitHubWebHookCrumbExclusion extends CrumbExclusion {

    @Override
    public boolean process(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String pathInfo = req.getPathInfo();
        if (isEmpty(pathInfo)) {
            return false;
        }
        // Github will not follow redirects https://github.com/isaacs/github/issues/574
        pathInfo = pathInfo.endsWith("/") ? pathInfo : pathInfo + '/';
        if (!pathInfo.equals(getExclusionPath())) {
            return false;
        }
        chain.doFilter(req, resp);
        return true;
    }

    public String getExclusionPath() {
        return "/" + GitHubWebHook.URLNAME + "/";
    }
}
