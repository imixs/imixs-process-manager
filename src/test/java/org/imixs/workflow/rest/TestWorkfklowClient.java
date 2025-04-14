package org.imixs.workflow.rest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.imixs.melman.BasicAuthenticator;
import org.imixs.melman.RestAPIException;
import org.imixs.melman.WorkflowClient;
import org.imixs.workflow.ItemCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test demonstrates the usage of the Melman Client API.
 * 
 * @author rsoika
 * 
 */
public class TestWorkfklowClient {

	static String BASE_URL = "http://localhost:8080/api";
	static String USERID = "admin";
	static String PASSWORD = "adminadmin";

	private IntegrationTest integrationTest = new IntegrationTest(BASE_URL);

	WorkflowClient workflowCLient = null;

	/**
	 * The setup method just tests if the Rest API is available. If not the test
	 * will be skipped!
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	public void setup() throws Exception {
		// Assumptions for integration tests
		assumeTrue(integrationTest.connected());
		workflowCLient = new WorkflowClient(BASE_URL);
		// create and register the default basic authenticator
		workflowCLient.registerClientRequestFilter(new BasicAuthenticator(USERID, PASSWORD));
	}

	/**
	 * test a client connection by creating a new workitem
	 * 
	 * Note: this test is skipped if the rest api sever is not running!
	 */
	@Test
	public void testCreateWorkitem() {
		assertNotNull(workflowCLient);
		ItemCollection workitem = null;
		try {
			workitem = new ItemCollection();
			workitem.model("ticket-model-1.0.0").task(1000).event(10);
			workitem.replaceItemValue("type", "workitem");
			// add some data..
			workitem.replaceItemValue("_ticketid", "TEST-ID-001");
			workitem.replaceItemValue("_problem", "Just a JUnit Test");

			// process workitem
			workitem = workflowCLient.processWorkitem(workitem);
			String uniqueID = workitem.getUniqueID();
			assertNotNull(uniqueID);

		} catch (RestAPIException e) {
			fail(e.getMessage());
		}
	}
}
