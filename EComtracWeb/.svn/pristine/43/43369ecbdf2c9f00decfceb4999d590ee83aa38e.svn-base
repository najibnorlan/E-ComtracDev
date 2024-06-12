package com.ssm.supplyinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpParameters;
import com.ssm.llp.base.common.model.LlpPaymentFee;
import com.ssm.llp.base.common.service.LlpPaymentFeeService;
import com.ssm.llp.base.page.BasePage;
import com.ssm.llp.base.wicket.component.SSMAjaxButton;
import com.ssm.llp.base.wicket.component.SSMDropDownChoice;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.base.wicket.component.SSMTextField;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class SupplyInfoMainPage extends BasePage {
	
	@SpringBean(name = "LlpPaymentFeeService")
	private LlpPaymentFeeService llpPaymentFeeService;
	
	public SupplyInfoMainPage() {

		addOrReplace(new SupplyInfoSecondSegmentPanel("secondSegment"));
		addOrReplace(new SupplyInfoThirdSegmentPanel("thirdSegment"));

		setDefaultModel(new CompoundPropertyModel(new LoadableDetachableModel() {
			protected Object load() {
				SupplyInfoCartModel supplyInfoCartModel = new SupplyInfoCartModel();
				return supplyInfoCartModel;
			}
		}));
		add(new SupplyInfoSearchForm("form", (IModel<SupplyInfoCartModel>) getDefaultModel()));
	}

	private class SupplyInfoSearchForm extends Form implements Serializable {
		private SearchCriteria sc2;
		
		
		public SupplyInfoSearchForm(String id, IModel<SupplyInfoCartModel> m) {
			super(id, m);
			final SupplyInfoCartModel searchModel = m.getObject();
			
			List<LlpParameters> listPersonalProd = getCodeType(Parameter.SUPPLY_INFO_PROD_TYPE_PERSONAL);
			
			List<SupplyInfoCartModel2> listProduct = new ArrayList();
			
			
			final Map<String, LlpPaymentFee> mapListProductFee = new HashMap<String, LlpPaymentFee>();
			for (int i = 0; i < listPersonalProd.size(); i++) {
				LlpPaymentFee llpPaymentFee = llpPaymentFeeService.findById(listPersonalProd.get(i).getCode());
				SupplyInfoCartModel2 model = new SupplyInfoCartModel2("PERSONAL",listPersonalProd.get(i).getCode(),listPersonalProd.get(i).getCodeDesc(), llpPaymentFee.getPaymentFee());
				listProduct.add(model);
				mapListProductFee.put(listPersonalProd.get(i).getCode(), llpPaymentFee);
			}
			
			final WebMarkupContainer wmcProduct = new WebMarkupContainer("wmcProduct");
			wmcProduct.setOutputMarkupId(true);
			wmcProduct.setOutputMarkupPlaceholderTag(true);
			wmcProduct.setVisible(false);
			add(wmcProduct);
			
			final SSMLabel entityNo = new SSMLabel("entityNo","");
//			wmcProduct.add(entityNo);
			
			final SSMLabel entityName = new SSMLabel("entityName","");
//			wmcProduct.add(entityName);
			
			
			
			final List<LlpParameters> listLocale = new ArrayList<LlpParameters>();
			listLocale.addAll(getCodeType(Parameter.SUPPLY_INFO_PROD_LOCALE));
			listLocale.add(new LlpParameters(Parameter.SUPPLY_INFO_PROD_LOCALE_BOTH,Parameter.SUPPLY_INFO_PROD_LOCALE_BOTH));

			final ListView<SupplyInfoCartModel2> listProductCart = new ListView<SupplyInfoCartModel2>("listProductCart", listProduct) {
				@Override
				protected void populateItem(ListItem<SupplyInfoCartModel2> item) {
					final SupplyInfoCartModel2 model = item.getModelObject();
					
					item.add(new SSMLabel("prodDesc", model.getProdDesc()));
					item.add(new SSMLabel("price", model.getPrice()));
					
					
					final SSMDropDownChoice prodLocale = new SSMDropDownChoice("prodLocale",new PropertyModel("", ""), listLocale);
					item.add(prodLocale);
					prodLocale.setDefaultModelObject(Parameter.SUPPLY_INFO_PROD_LOCALE_ENG);
					
					final SSMAjaxButton addButton = new SSMAjaxButton("addButton") {
						@Override
						protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
							SupplyInfoCartModel searchModel1 = (SupplyInfoCartModel) form.getModelObject();
							LlpPaymentFee fee = mapListProductFee.get(model.getProdCode());
							
							boolean successAdd = addToCart(Parameter.ID_TYPE_newic, searchModel1.getIdNo(), entityName.getDefaultModelObjectAsString(), Parameter.SUPPLY_INFO_PROD_TYPE_PERSONAL, model.getProdCode(), fee.getPaymentFee(), prodLocale.getInput(), target);
							if(successAdd) {
								target.appendJavaScript("addToCard('"+getMarkupId()+"')");
							}else {
								target.appendJavaScript("alert('Already in cart')");
							}
						}
					};
					item.add(addButton);
					
				}
			};
			wmcProduct.add(listProductCart);
			
			final SSMTextField idNo = new SSMTextField("idNo");
			add(idNo);
			
			OnChangeAjaxBehavior idNoOnchange = new OnChangeAjaxBehavior() {
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					System.out.println(idNo.getValue());
					if(idNo.getValue().length()>=12) {
						String name = supplyInfoTransHdrService.findEntityNameByTypeNNo(Parameter.ID_TYPE_newic, idNo.getValue());
						entityNo.setDefaultModelObject(idNo.getValue());
						entityName.setDefaultModelObject(name);
						
						wmcProduct.setVisible(true);
					}else {
						
						entityNo.setDefaultModelObject("");
						entityName.setDefaultModelObject("");
						
						wmcProduct.setVisible(false);
					}
					
					target.add(wmcProduct);
				}
			};
			idNo.add(idNoOnchange);
			
			
		}

	}
	
	
	@Override
	public boolean displayAnimatedBackground() {
		return true;
	}
	
	
	private class SupplyInfoCartModel2 implements Serializable {
		String prodType;
		String prodCode;
		String prodDesc;
		String prodLocale;
		
		double price;
		
		public SupplyInfoCartModel2(String prodType, String prodCode, String prodDesc, double price) {
			this.prodType=prodType;
			this.prodCode=prodCode;
			this.prodDesc=prodDesc;
			this.price=price;
		}
		
		public String getProdType() {
			return prodType;
		}
		public void setProdType(String prodType) {
			this.prodType = prodType;
		}
		public String getProdCode() {
			return prodCode;
		}
		public void setProdCode(String prodCode) {
			this.prodCode = prodCode;
		}
		public String getProdDesc() {
			return prodDesc;
		}
		public void setProdDesc(String prodDesc) {
			this.prodDesc = prodDesc;
		}
		public String getProdLocale() {
			return prodLocale;
		}
		public void setProdLocale(String prodLocale) {
			this.prodLocale = prodLocale;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		
		
	}

	private class SupplyInfoCartModel implements Serializable {
		String idNo;
		String entityName;

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

		public String getEntityName() {
			return entityName;
		}

		public void setEntityName(String entityName) {
			this.entityName = entityName;
		}

	

	}


	@Override
	public String getPageTitle() {
		return "com.ssm.supplyInfo.supplyInfoMain";
	}

}
