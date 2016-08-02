package guru.drinkit.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.NonceExpiredException;

/**
 * Copy-pasted class from parent with slight difference:
 * added "_" symbol before authenticateHeader for disabling browser authentication.
 */
@SuppressWarnings("ALL")
public class CustomDigestAuthenticationEntryPoint extends DigestAuthenticationEntryPoint {
    private static final Log logger = LogFactory.getLog(DigestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // compute a nonce (do not use remote IP address due to proxy farms)
        // format of nonce is:
        //   base64(expirationTime + ":" + md5Hex(expirationTime + ":" + key))
        long expiryTime = System.currentTimeMillis() + (getNonceValiditySeconds() * 1000);
        String signatureValue = md5Hex(expiryTime + ":" + getKey());
        String nonceValue = expiryTime + ":" + signatureValue;
        String nonceValueBase64 = new String(Base64.encode(nonceValue.getBytes()));

        // qop is quality of protection, as defined by RFC 2617.
        // we do not use opaque due to IE violation of RFC 2617 in not
        // representing opaque on subsequent requests in same session.
        String authenticateHeader = "_Digest realm=\"" + getRealmName() + "\", " + "qop=\"auth\", nonce=\""
                + nonceValueBase64 + "\"";

        if (authException instanceof NonceExpiredException) {
            authenticateHeader = authenticateHeader + ", stale=\"true\"";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("WWW-Authenticate header sent to user agent: " + authenticateHeader);
        }

        response.addHeader("WWW-Authenticate", authenticateHeader);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    public static long copy(InputStream from, OutputStream to)
            throws IOException {
//        checkNotNull(from);
//        checkNotNull(to);
        byte[] buf = new byte[0x1000];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    static private String md5Hex(String data) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(data.getBytes())));
    }
}
