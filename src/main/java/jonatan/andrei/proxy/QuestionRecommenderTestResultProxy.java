package jonatan.andrei.proxy;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "qr-api-test-result")
public interface QuestionRecommenderTestResultProxy extends QuestionRecommenderAbstractProxy {
}
