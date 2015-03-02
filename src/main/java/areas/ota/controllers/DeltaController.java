package areas.ota.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;

import utils.FileUtil;
import utils.ProjectUtil;

import com.google.inject.Inject;

import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.Version;
import models.JResponse;
import models.PageList;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.params.PathParam;
import controllers.BaseController;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;

@FilterWith(AuthorizationFilter.class)
public class DeltaController extends BaseController {
	@Inject
	private IDatabase<Delta> dao;
	@Inject
	private IDatabase<Version> versionDao;

	@Inject
	public DeltaController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public Result list(
			@Param("page") int page,
			@Param("versionId") long versionId) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<Delta> query = dao.all();
		if (versionId > 0) 
			query.and(c -> c.equals("fromVersionId", versionId))
				 .or(c -> c.equals("toVersionId", versionId));
		
		PageList<Delta> entities = query.toPageList(link, page, 10);
		
		return Results.html().render("deltas", entities);
	}
	
	
	
	public Result add() {
		
		return Results.html();
	}
	
	public Result save(Delta delta) {
		return Results.ok();
	}
	
	public Result delete(@Param(value="id") long id) {
		Delta entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			dao.delete(entity).commit();
			return Results.json().render(JResponse.success("ota/delta"));
		}
	}
	
	public Result setStatus(@Param(value="id") long id, @Param(value="status") int status) {
		Delta entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			entity.setStatus(status);
			dao.update(entity).commit();
			return Results.json().render(JResponse.success("ota/delta"));
		}
	}
	
	public Result uploadfile(
			Context context,
			@PathParam(value="deltaId") long deltaId
		) throws FileUploadException, IOException  {
		// Make sure the context really is a multipart context...
		if (context.isMultipart()) {
	        // This is the iterator we can use to iterate over the
	        // contents of the request.
	        FileItemIterator fileItemIterator = context
	                .getFileItemIterator();
	        while (fileItemIterator.hasNext()) {
	            FileItemStream item = fileItemIterator.next();
	            	
	            String name = item.getFieldName();
	            InputStream stream = item.openStream();
	            String contentType = item.getContentType();
	            
	            Delta delta = dao.first(c -> c.equals("id", deltaId));
            	if (delta != null) {
            		String path = ProjectUtil.getDeltaSavePath() + String.valueOf(delta.getId());
                	System.out.println(path+contentType);
                	FileUtil.mkdirs(path);
                	File outputFile = new File(path+File.separator+item.getName());
                	if (outputFile.exists()) outputFile.delete();
                	FileUtil.fileSaveAs(stream, outputFile);
                	delta.setFilePath(item.getName());
                	delta.setSize(outputFile.length());
                	delta.setStatus(Delta.Status.TESTING);
                	dao.update(delta).commit();

            		return Results.json().render(JResponse.success("ota/delta"));
            	}
	        }
	    }
		
		return Results.json().render(JResponse.fail("读取不到数据，请刷新页面重试"));
	} 



}
