/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portal.kernel.workflow;

import com.liferay.portal.kernel.messaging.proxy.MessagingProxy;
import com.liferay.portal.kernel.messaging.proxy.ProxyMode;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;
import java.util.Map;

@MessagingProxy(mode = ProxyMode.SYNC)
/**
 * <a href="WorkflowDefinitionManager.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * The workflow definition manager is used to deploy workflow definitions. A
 * workflow definition is the process model of a workflow to be executed by
 * creating a workflow instance attached to it, reflecting the current state and
 * history as well as any related objects like tasks, jobs and timers.
 * </p>
 *
 * <p>
 * Depending on the underlying workflow engine, even hot deployment is possible.
 * </p>
 *
 * @author Micha Kiener
 */
public interface WorkflowDefinitionManager {

	/**
	 * <p>
	 * Deploys the given workflow definition within the engine.
	 * </p>
	 *
	 * <p>
	 * If the workflow definition already exists and versioning is not supported
	 * or it is the same version as already existing, the definition is
	 * exchanged with the given one, otherwise the definition is added as a
	 * complete new definition or as a new version of an already existing
	 * definition.
	 * </p>
	 *
	 * <p>
	 * If you deploy a workflow definition by overwriting an existing one, make
	 * sure it is compatible with already existing workflow instances to not
	 * compromise their execution plan or current state, tasks or timers. It is
	 * usually a good practice to deploy changed workflow definitions as new
	 * versions, so that existing workflow instances are being finished with the
	 * old definition, and newly created workflow instances are created by using
	 * the new version.
	 * </p>
	 *
	 * <p>
	 * You can actually choose whether the engine should create a new version
	 * number automatically, or if the one provided by the
	 * <code>workflowDefinition</code> should be used, however, you have to make
	 * sure in this case, that the version number is valid (a positive integer
	 * value), otherwise this method will throw an exception.
	 * </p>
	 *
	 * @param  workflowDefinition the workflow definition to be deployed
	 * @param  callingUserId the id of the user deploying the workflow
	 *		   definition (see {@link
	 *		   UserCredentialFactoryUtil#createCredential(long)} for more
	 *		   information)
	 * @param  autoIncrementVersionNumber <code>true</code>, if the workflow
	 *		   engine should automatically create the next version number for
	 *		   the definition being deployed or <code>false</code>, if the
	 *		   version number is provided within the
	 *		   <code>workflowDefinition</code> parameter
	 * @param  parameters any additional parameters the underlying engine would
	 *		   need to deploy the given definition (like the name of the
	 *		   repository or anything similar), if nothing is needed, this
	 *		   parameter can be left <code>null</code>
	 * @throws WorkflowException is thrown, if deployment of the definition
	 *		   failed or if the provided workflow definition version number was
	 *		   wrong
	 */
	public void deployWorkflowDefinition(
			WorkflowDefinition workflowDefinition,
			@CallingUserId long callingUserId,
			boolean autoIncrementVersionNumber, Map<String, Object> parameters)
		throws WorkflowException;

	/**
	 * Returns the count of workflow definitions being deployed within the
	 * repository.
	 *
	 * @return the count of available workflow definitions
	 * @throws WorkflowException is thrown, if requesting the information is not
	 *		   possible
	 */
	public int getWorkflowDefinitionCount() throws WorkflowException;

	/**
	 * Returns the count of workflow definition versions for a specific workflow
	 * definition name. If the workflow definition specified is not available,
	 * this method just returns 0.
	 *
	 * @param  workflowDefinitionName the name of the workflow definition to
	 *		   return its count of versions
	 * @return the number of deployed versions for the specified workflow
	 *		   definition
	 * @throws WorkflowException is thrown on any exception while requesting the
	 *		   information
	 */
	public int getWorkflowDefinitionCount(String workflowDefinitionName)
		throws WorkflowException;

	/**
	 * Returns a list of all workflow definitions available within the
	 * repository. The returned list will contain information objects about the
	 * workflow definitions but without the definition model file actually, so
	 * {@link WorkflowDefinition#getJar()} will always return <code>null</code>.
	 * The list will only contain the newest (actual) version of a definition,
	 * if you need all versions for a specific version, use the method {@link
	 * #getWorkflowDefinitions(String, int, int, OrderByComparator)} instead
	 * where all versions for a specific workflow definition are being returned.
	 *
	 * @param  start inclusive start position for paginating the result
	 * @param  end exclusive end position for paginating the result
	 * @param  orderByComparator comparator for sorting the result
	 * @return the list of available workflow definitions, never
	 *		   <code>null</code>
	 */
	public List<WorkflowDefinition> getWorkflowDefinitions(
		int start, int end, OrderByComparator orderByComparator);

	/**
	 * Returns a list of all versions of the specified workflow definition, if
	 * found, an empty list otherwise or a list containing just one element, if
	 * there is only one version available or versioning is not supported at all
	 * by the underlying workflow engine.
	 *
	 * @param  workflowDefinitionName the name of the workflow definition to
	 *		   retrieve all versions for
	 * @param  start inclusive start position for paginating the result
	 * @param  end exclusive end position for paginating the result
	 * @param  orderByComparator comparator for sorting the result
	 * @return the list of all versions, if any found, an empty list otherwise
	 *		   or a list containing just one element, must never be
	 *		   <code>null</code>
	 */
	public List<WorkflowDefinition> getWorkflowDefinitions(
		String workflowDefinitionName, int start, int end,
		OrderByComparator orderByComparator);

}