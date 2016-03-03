/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.fit.console;

import static org.apache.syncope.fit.console.AbstractConsoleITCase.KEY;
import static org.junit.Assert.assertNull;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.syncope.client.console.pages.SecurityQuestions;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM)
public class SecurityQuestionsITCase extends AbstractConsoleITCase<Long> {

    @Before
    public void login() {
        doLogin(ADMIN_UNAME, ADMIN_PWD);
        wicketTester.clickLink("body:configurationLI:configurationUL:securityquestionsLI:securityquestions");
        wicketTester.assertRenderedPage(SecurityQuestions.class);
    }

    @Test
    public void read() {
        wicketTester.assertLabel(
                "body:content:securityQuestionPanel:container:content:searchContainer:resultTable:"
                + "tablePanel:groupForm:checkgroup:dataTable:body:rows:1:cells:2:cell",
                "What&#039;s your mother&#039;s maiden name?");

        wicketTester.assertComponent(
                "body:content:securityQuestionPanel:container:content:"
                + "searchContainer:resultTable:tablePanel:"
                + "groupForm:checkgroup:dataTable:body:rows:"
                + "1:cells:3:cell:panelEdit:editLink", IndicatingAjaxLink.class);
    }

    @Test
    public void create() {
        wicketTester.clickLink("body:content:securityQuestionPanel:container:content:add");

        wicketTester.assertComponent(
                "body:content:securityQuestionPanel:modal", Modal.class);

        final FormTester formTester = wicketTester.newFormTester("body:content:securityQuestionPanel"
                + ":modal:form");
        formTester.setValue("content:securityQuestionDetailsPanel:container:form:content:textField",
                "What's your favorite team?");

        wicketTester.clickLink("body:content:securityQuestionPanel:modal:dialog:footer:inputs:0:submit");

        wicketTester.assertInfoMessages("Operation executed successfully");
        wicketTester.cleanupFeedbackMessages();
    }

    @Test
    public void update() {
        Component result = findComponentByProp(KEY, "body:content:securityQuestionPanel:container:content:"
                + "searchContainer:resultTable", 100L);

        wicketTester.assertLabel(
                result.getPageRelativePath() + ":cells:1:cell", "100");

        wicketTester.clickLink(
                result.getPageRelativePath() + ":cells:3:cell:panelEdit:editLink");

        final FormTester formTester = wicketTester.newFormTester("body:content:securityQuestionPanel"
                + ":modal:form");
        formTester.setValue("content:securityQuestionDetailsPanel:container:form:content:textField",
                "What's your favorite car?");

        wicketTester.clickLink("body:content:securityQuestionPanel:modal:dialog:footer:inputs:0:submit");

        wicketTester.assertInfoMessages("Operation executed successfully");
        wicketTester.cleanupFeedbackMessages();
    }

    @Test
    public void delete() {
        Component result = findComponentByProp(KEY, "body:content:securityQuestionPanel:container:content:"
                + "searchContainer:resultTable:", 100L);

        wicketTester.assertLabel(
                result.getPageRelativePath() + ":cells:1:cell", "100");

        wicketTester.getRequest().addParameter("confirm", "true");
        wicketTester.clickLink(
                wicketTester.getComponentFromLastRenderedPage(
                        result.getPageRelativePath() + ":cells:3:cell:panelDelete:deleteLink"));

        wicketTester.executeAjaxEvent(wicketTester.getComponentFromLastRenderedPage(
                result.getPageRelativePath() + ":cells:3:cell:panelDelete:deleteLink"), "onclick");

        wicketTester.assertInfoMessages("Operation executed successfully");
        wicketTester.cleanupFeedbackMessages();

        assertNull(findComponentByProp(KEY,
                "body:content:securityQuestionPanel:container:content:"
                + "searchContainer:resultTable:"
                + "tablePanel:groupForm:checkgroup:dataTable", 100L));
    }
}
