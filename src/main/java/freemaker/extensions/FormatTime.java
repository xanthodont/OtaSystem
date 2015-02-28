package freemaker.extensions;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

@SuppressWarnings("deprecation")
public class FormatTime implements TemplateMethodModel {

	@Override
	public Object exec(List args) throws TemplateModelException {
		// TODO Auto-generated method stub
		if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
		}
		
		return "format";
	}

}
