/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
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

package com.liferay.test.service;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocatorUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionCheckerFactory;
import com.liferay.portal.security.permission.PermissionCheckerImpl;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.test.TestCase;
import com.liferay.test.TestProps;
import com.liferay.util.GetterUtil;

/**
 * <a href="BaseServiceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael Young
 *
 */
public class BaseServiceTest extends TestCase {

	protected void setUp() throws Exception {
		BeanLocatorUtil.setBeanLocator(new BeanLocatorImpl());

		String remoteUser = TestProps.get("service.user.id");

		PrincipalThreadLocal.setName(remoteUser);

		long userId = GetterUtil.getLong(remoteUser);

		User user = UserLocalServiceUtil.getUserById(userId);

		_permissionChecker = PermissionCheckerFactory.create(user, true, true);

		PermissionThreadLocal.setPermissionChecker(_permissionChecker);
	}

	protected void tearDown() throws Exception {
		PermissionCheckerFactory.recycle(_permissionChecker);
	}

	private PermissionCheckerImpl _permissionChecker = null;

}