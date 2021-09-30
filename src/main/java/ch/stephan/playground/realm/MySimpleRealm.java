package ch.stephan.playground.realm;

import java.security.Principal;
import java.security.spec.AlgorithmParameterSpec;
import org.wildfly.security.auth.SupportLevel;
import org.wildfly.security.auth.server.RealmIdentity;
import org.wildfly.security.auth.server.SecurityRealm;
import org.wildfly.security.credential.Credential;
import org.wildfly.security.evidence.Evidence;
import org.wildfly.security.evidence.PasswordGuessEvidence;

public class MySimpleRealm implements SecurityRealm {

  @Override
  public RealmIdentity getRealmIdentity(final Principal principal) {
    System.out.println(String.format("Creating a MySimpleRealm for user %s.", principal));
    return new MySimpleRealmIdentity(principal);
  }

  @Override
  public SupportLevel getEvidenceVerifySupport(Class<? extends Evidence> evidenceType, String algorithmName) {
    return PasswordGuessEvidence.class.isAssignableFrom(evidenceType) ? SupportLevel.POSSIBLY_SUPPORTED : SupportLevel.UNSUPPORTED;
  }

  @Override
  public SupportLevel getCredentialAcquireSupport(Class<? extends Credential> credentialType, String algorithmName, AlgorithmParameterSpec parameterSpec) {
    return SupportLevel.UNSUPPORTED;
  }

}
