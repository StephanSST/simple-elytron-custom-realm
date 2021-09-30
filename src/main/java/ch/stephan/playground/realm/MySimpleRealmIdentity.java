package ch.stephan.playground.realm;

import java.security.Principal;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.wildfly.security.auth.SupportLevel;
import org.wildfly.security.auth.server.RealmIdentity;
import org.wildfly.security.auth.server.RealmUnavailableException;
import org.wildfly.security.authz.Attributes;
import org.wildfly.security.authz.AuthorizationIdentity;
import org.wildfly.security.authz.MapAttributes;
import org.wildfly.security.credential.Credential;
import org.wildfly.security.evidence.Evidence;
import org.wildfly.security.evidence.PasswordGuessEvidence;

public class MySimpleRealmIdentity implements RealmIdentity {

  private final Principal principal;

  public MySimpleRealmIdentity(Principal principal) {
    this.principal = principal;
  }

  @Override
  public boolean verifyEvidence(Evidence evidence) throws RealmUnavailableException {
    System.out.println(String.format("Doing a password check for user %s.", principal.getName()));
    if (evidence instanceof PasswordGuessEvidence) {
      PasswordGuessEvidence guess = (PasswordGuessEvidence) evidence;
      try {
        String username = principal.getName();
        String password = new String(guess.getGuess());
        return username.equals(password);
      } catch (Exception ex) {
        System.out.println("Technical problem with Keycloak configuration");
        ex.printStackTrace();
      } finally {
        guess.destroy();
      }
    }
    return false;
  }

  @Override
  public Attributes getAttributes() {
    List<String> roleList = new ArrayList<>(Arrays.asList("AuthenticatedRole"));
    if ("admin".equals(principal.getName())) {
      roleList.add("admin");
    }

    Attributes map = new MapAttributes();
    map.addAll("Roles", roleList);
    return map;
  }

  @Override
  public AuthorizationIdentity getAuthorizationIdentity() {
    return AuthorizationIdentity.basicIdentity(this::getAttributes, "Baloise");
  }

  @Override
  public Principal getRealmIdentityPrincipal() {
    return principal;
  }

  @Override
  public boolean exists() {
    return true;
  }

  @Override
  public SupportLevel getCredentialAcquireSupport(Class<? extends Credential> credentialType, String algorithmName, AlgorithmParameterSpec parameterSpec) {
    return SupportLevel.UNSUPPORTED;
  }

  @Override
  public <C extends Credential> C getCredential(Class<C> credentialType) {
    return null;
  }

  @Override
  public SupportLevel getEvidenceVerifySupport(Class<? extends Evidence> evidenceType, String algorithmName) throws RealmUnavailableException {
    return PasswordGuessEvidence.class.isAssignableFrom(evidenceType) ? SupportLevel.POSSIBLY_SUPPORTED : SupportLevel.UNSUPPORTED;
  }

}
