package com.ssm.ezbiz.myCardTransaction;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Path;

import com.ssm.ezbiz.comtrac.ListComtracTraining;
import com.ssm.ezbiz.service.SSMMyKadModelService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.page.SecBasePage;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.base.wicket.component.SSMAjaxLink;
import com.ssm.llp.base.wicket.component.SSMLabel;
import com.ssm.llp.ezbiz.model.SSMMyKadModel;

@SuppressWarnings({"all"})
public class MyCardDetailPage extends SecBasePage{
	
	@SpringBean(name = "SSMMyKadModelService")
	SSMMyKadModelService ssmMyKadModelService;
	public MyCardDetailPage(final long mykadId){
		
		final SSMMyKadModel myKadModel = ssmMyKadModelService.findById(mykadId);
		
		String contextPath = WicketApplication.get().getServletContext().getServletContextName();
		String js = "function CallPrint(strid) {";
		js += "var prtContent = document.getElementById(strid);";
		js += "var WinPrint = window.open('', '','left=0,top=0,width=800,height=600,toolbar=0,scrollbars=0,status=0');";
		js += "WinPrint.document.write('<style>@font-face { font-family: \"BarcodeC39\"; src:url(" + contextPath + "/css/BarcodeC39.otf\") format(\"TrueType\");}</style>');";
		js += "WinPrint.document.write('<link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/semantic/semantic.min.css \"> <link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/css/styles.css \" /> <link rel=\"stylesheet\" type=\"text/css\" href=" + contextPath + "/semantic/components/form.min.css \">');";
		js += "WinPrint.document.write('<style>body{padding:0px 20px 0px 20px; margin: 0px}td{font-family:\"Tahoma\",\"LucidaSans\",\"Arial\",\"Helvetica\",\"Sans-serif\",\"sans\";font-size:9pt;padding:2px;}table#itemsTable{border:0.5px solid black;}table#itemsTable td{padding:4px;}table#gstSummary td{padding-right:50px;}</style>');";
		js += "WinPrint.document.write(prtContent.innerHTML);";
		js += "WinPrint.document.close();";
		js += "WinPrint.focus();";
		js += "setTimeout(initprint, 200);";
		js += "function initprint(){WinPrint.print();WinPrint.close();}}";
		
		Label jsScript = new Label("jsScript", js);
		jsScript.setEscapeModelStrings(false);
		jsScript.setOutputMarkupId(true);
		
		add(jsScript);
		
		add(new SSMLabel("name" , myKadModel.getName()));
		add(new SSMLabel("originalName" , myKadModel.getOriginalName()));
		add(new SSMLabel("gmpcName" , myKadModel.getGmpcName()));
		add(new SSMLabel("mykadNo", myKadModel.getMykadNo()));
		add(new SSMLabel("dob", myKadModel.getDob()));
		add(new SSMLabel("birthPlace", myKadModel.getBirthPlace()));
		add(new SSMLabel("citizenship", myKadModel.getCitizenship()));
		add(new SSMLabel("race", myKadModel.getRace()));
		add(new SSMLabel("religion", myKadModel.getReligion()));
		add(new SSMLabel("gender", myKadModel.getGender(), Parameter.GENDER));
		add(new SSMLabel("thumbPrintSuccess", myKadModel.getThumbPrintSuccess(),Parameter.YES_NO));
		add(new SSMLabel("createDt", myKadModel.getCreateDt()));
		add(new SSMLabel("createBy", myKadModel.getCreateBy()));
		add(new SSMLabel("adUserRequester",myKadModel.getAdUserRequester()));
		add(new SSMLabel("ipAddress", myKadModel.getIpAddress()));	
		
		System.out.println("myKadModel.getMykadPicture() : " + myKadModel.getMykadPicture());
		IResource imageResource = null;
		
		if(myKadModel.getMykadPicture() != null){
			imageResource =  new DynamicImageResource() {
				@Override
				protected byte[] getImageData(IResource.Attributes attributes) {
					return myKadModel.getMykadPicture();
					
				}
			};
		}else{
			imageResource = new ContextRelativeResource("images/noimagefound.jpg");
		}
		
		
		NonCachingImage image = new NonCachingImage("wicketId", imageResource);
		this.add(image);
		
		String address = myKadModel.getAddress1();
		if(StringUtils.isNotBlank(myKadModel.getAddress2())){
			address += "\n"+myKadModel.getAddress2();
		}
		if(StringUtils.isNotBlank(myKadModel.getAddress3())){
			address += "\n"+myKadModel.getAddress3();
		}
		
		address = address;
		add(new MultiLineLabel("address", address));
		
		add(new SSMLabel("postcode", myKadModel.getPostcode()));
		add(new SSMLabel("city", myKadModel.getCity()));
		add(new SSMLabel("state", myKadModel.getState()));
		
		SSMAjaxLink previous = new SSMAjaxLink("previous"){
			@Override
			public void onClick(AjaxRequestTarget target){
				
				setResponsePage(new SearchMyCardList());
			}
		};
		add(previous);
		
		}
	
	@Override
	public String getPageTitle() {
		return "page.title.ezbiz.myCardDetailPage";
	}

}
