package com.ssm.llp.base.page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;


/**
 * Authenticated session subclass. Note that it is derived from AuthenticatedWebSession which is
 * defined in the auth-role module.
 * 
 * @author ZamZam
 */
public class InternalAuthenticatedWebSession extends AuthenticatedWebSession
{
    /**
     * Construct.
     * 
     * @param request
     *            The current request object
     */
    public InternalAuthenticatedWebSession(Request request)
    {
        super(request);
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean authenticate(final String username, final String password)
    {
//        final String WICKET = "wicket";
//
//        LlpUserProfileService llpUserProfileService = (LlpUserProfileService) WicketApplication.getServiceNew(LlpUserProfileService.class.getSimpleName());
//        
//        try {
//			LlpUserProfile llpUserProfile = llpUserProfileService.getProfileLogin(username, password);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return false;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles()
    {
        if (isSignedIn())
        {
            // If the user is signed in, they have these roles
            return new Roles(Roles.ADMIN);
        }
        return null;
    }
}
