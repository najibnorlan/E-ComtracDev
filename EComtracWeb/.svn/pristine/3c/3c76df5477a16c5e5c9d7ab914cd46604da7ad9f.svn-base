/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ssm.llp.wicket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.Method;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

/**
 * Ajax event behavior that submits a form via ajax when the event it is attached to, is invoked.
 * <p>
 * The form must have an id attribute in the markup or have MarkupIdSetter added.
 * 
 * @see AjaxEventBehavior
 * 
 * @since 1.2
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public abstract class SSMAjaxFormSubmitBehavior extends AjaxFormSubmitBehavior
{
	public List<FormComponent> listReq = new ArrayList();
	boolean byPassValidation = false;
	
	public SSMAjaxFormSubmitBehavior(String event) {
		this(event, false);
	}
	
	public SSMAjaxFormSubmitBehavior(String event, boolean byPassValidation) {
		super(event);
		this.byPassValidation = byPassValidation;
	}
	
	@Override
	protected void onAfterSubmit(AjaxRequestTarget target) {
		super.onAfterSubmit(target);
		if(byPassValidation){
			for (int i = 0; i < listReq.size(); i++) {
				listReq.get(i).setRequired(true);
			}
		}
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
//		System.out.println(getEvent());
//		System.out.println(byPassValidation);
		if (byPassValidation && "change".equals(getEvent())) {
			listReq = new ArrayList();
			Object obj = getComponent().getParent();
			if (obj instanceof Form) {
				ComponentHierarchyIterator hieracy = ((Form) obj).visitChildren(FormComponent.class);
				for (Iterator iterator = hieracy.iterator(); iterator.hasNext();) {
					listReq = new ArrayList();
					FormComponent formComponent1 = (FormComponent) iterator.next();
					boolean required = formComponent1.isRequired();
					if (required) {
						formComponent1.setRequired(false);
						listReq.add(formComponent1);
					}
					formComponent1.modelChanging();
					formComponent1.validate();
					formComponent1.updateModel();
					formComponent1.modelChanged();
				}
			}
		}
		super.onEvent(target);
	}
	
}
