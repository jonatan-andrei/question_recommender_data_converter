package jonatan.andrei.proxy;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "qr-api")
public interface QuestionRecommenderProxy extends QuestionRecommenderAbstractProxy {

}
