package jonatan.andrei.proxy;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "qr-api-database")
public interface QuestionRecommenderDatabaseProxy extends QuestionRecommenderAbstractProxy {

}
