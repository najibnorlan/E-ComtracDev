package com.ssm.llp.base.page;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.ssm.llp.base.wicket.component.SSMAjaxCheckBox;
import com.ssm.llp.base.wicket.component.SSMLabel;

public class TestPage extends WebPage implements Serializable {
	public TestPage() {
		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
            protected Object load() {
            	return new TestClass();
            }
        }));
		add(new TestForm("testForm",getDefaultModel()));
	}
	
	private class TestForm extends Form  {
		public TestForm(String id, IModel m) {
			super(id, m);
			final TestClass testClass = (TestClass) m.getObject();
			
			final SSMLabel lbl = new SSMLabel("lbl","");
			lbl.setOutputMarkupId(true);
			add(lbl);
			SSMAjaxCheckBox isB1 = new SSMAjaxCheckBox("addr", new PropertyModel(testClass, "isB1") ) {
				@Override
				protected void onUpdate(AjaxRequestTarget arg0) {
//					if (String.valueOf(true).equals(getValue())) {
//						robFormB.setIsB1(true);
//						wmcB1.setVisible(true);
//					}else{
//						robFormB.setIsB1(false);
//						wmcB1.setVisible(false);
//					}
//					arg0.add(wmcB1);
					System.out.println("yezza");
					lbl.setDefaultModelObject("success"+System.currentTimeMillis());
					arg0.add(lbl);
				}
				
				@Override
			    protected void updateAjaxAttributes( AjaxRequestAttributes attributes )
			    {
			        super.updateAjaxAttributes( attributes );
			        AjaxCallListener ajaxCallListener = new AjaxCallListener();
			        
			        
			        String str = "";//alert('masuk');alert($('#" + getMarkupId() + "'));";
//			        str +="if(!document.getElementById('"+getMarkupId()+"').checked){";
//			        str +=" if (jQuery.data( "+getMarkupId()+", 'isOk' ) != 'OK')  { ";
//			        str +="		alertify.confirm('Confirm Title', 'Confirm Message', ";
//					str +="			function(){ ";
//					str +="				jQuery.data( "+getMarkupId()+", 'isOk', 'OK' ); ";
//					str +="				document.getElementById('"+getMarkupId()+"').checked=false;";
//					str +=" 			$('#" + getMarkupId() + "').trigger('click'); ";
//					str +="			},";
//					str +="			function(){ ";
//					str +="				document.getElementById('"+getMarkupId()+"').checked=true;";
//					str +="			}";
//					str +="		);";
//			        str +="		return false; "; 
//			        str +=" } else { ";
//			        str +="		jQuery.removeData( "+getMarkupId()+", 'isOk'); ";
//			        str +="		document.getElementById('"+getMarkupId()+"').checked=false;alert('falsekan');";
//			        str +="		return true;  "; 
//			        str +=" }"; 
//			        str +="}"; 
			        
			        
			        str +=" var isOk = (jQuery.data( "+getMarkupId()+", 'isOk' ) == 'OK'); ";
			        str +=" var isCheck = document.getElementById('"+getMarkupId()+"').checked; ";
			        
			        str +="	jQuery.removeData( "+getMarkupId()+", 'isOk'); ";
			        str +=" if(!isCheck){ ";
			        str +=" 	if(!isOk){ ";	
			        str +="				document.getElementById('"+getMarkupId()+"').checked=true;";
			        str +="				alertify.confirm('Confirm Title', 'Confirm Message', ";
					str +="						function(){ ";
					str +="							jQuery.data( "+getMarkupId()+", 'isOk', 'OK' ); ";
//					str +="							document.getElementById('"+getMarkupId()+"').checked=false;";
					str +=" 						$('#" + getMarkupId() + "').trigger('click'); ";
					str +="						},";
					str +="						function(){ ";
					str +="							document.getElementById('"+getMarkupId()+"').checked=true;";
					str +="						}";
					str +="				);";
			        str +=" 	} else { ";	
			        str +=" 		return true; ";	
			        str +=" 	} ";
			        str +=" } else {";
			        str +=" 	return true; ";			
			        str +=" } ";
			        str +=" return false;";
			        
			        
			        
			        
			        ajaxCallListener.onPrecondition( str );
			        attributes.getAjaxCallListeners().add( ajaxCallListener );
			        
			    }
			};
			add(isB1);
		}
		

	}
}

class TestClass implements Serializable{
	private Boolean isB1 = Boolean.FALSE;

	public Boolean getIsB1() {
		return isB1;
	}

	public void setIsB1(Boolean isB1) {
		this.isB1 = isB1;
	}
	
	
}
