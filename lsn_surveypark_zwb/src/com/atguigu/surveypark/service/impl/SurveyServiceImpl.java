package com.atguigu.surveypark.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.atguigu.surveypark.dao.BaseDao;
import com.atguigu.surveypark.model.Answer;
import com.atguigu.surveypark.model.Page;
import com.atguigu.surveypark.model.Question;
import com.atguigu.surveypark.model.Survey;
import com.atguigu.surveypark.model.User;
import com.atguigu.surveypark.service.SurveyService;
import com.atguigu.surveypark.util.DataUtil;

/**
 * SurveyService实现
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

	@Resource(name="surveyDao")//Resource是先按照名称注入，然后再按照类型注入
	private BaseDao<Survey> surveyDao ;
	
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao ;
	
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao ;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao ;
	
	/**
	 * 查询调查集合
	 */
	public List<Survey> findMySurveys(User user) {
		String hql = "from Survey s where s.user.id = ?" ;
		return surveyDao.findEntityByHQL(hql,user.getId());
	}
	
	/**
	 * 新建调查
	 */
	public Survey newSurvey(User user){
		Survey s = new Survey();
		Page p = new Page();
		
		//设置关联关系
		s.setUser(user);
		p.setSurvey(s);
		s.getPages().add(p);
		
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s ;
	}
	
	/**
	 * 按照id查询Survey
	 */
	public Survey getSurvey(Integer sid){
		return surveyDao.getEntity(sid);
	}
	
	/**
	 * 按照id查询Survey,同时携带所有的孩子
	 */
	public Survey getSurveyWithChildren(Integer sid){
		Survey s = this.getSurvey(sid);
		//强行初始化pages和questions集合
		for(Page p : s.getPages()){
			p.getQuestions().size();
		}
		return s; 
	}
	
	/**
	 * 更新调查
	 */
	public void updateSurvey(Survey model){
		surveyDao.updateEntity(model);
	}
	
	/**
	 * 保存/更新页面
	 */
	public void saveOrUpdatePage(Page model){
		pageDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * 按照id查询页面
	 */
	public Page getPage(Integer pid){
		return pageDao.getEntity(pid);
	}
	
	/**
	 * 保存/更新问题
	 */
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	/**
	 * 非常关键：开发中不使用hibernate的级联删除（级联操作）的原因是：级联操作的性能较差------------------------------------关键：级联操作，hibernate将级联的孩子查出来，一个一个干掉
	 * 删除问题,同时删除答案
	 */
	public void deleteQuestion(Integer qid){
		//1.删除answers
		String hql = "delete from Answer a where a.questionId = ?" ;
		answerDao.batchEntityByHQL(hql,qid);
		//2.delete question
		hql = "delete from Question q where q.id = ?" ;
		questionDao.batchEntityByHQL(hql,qid);
	}
	
	/**
	 * 删除page,同时删除问题,答案
	 */
	public void deletePage(Integer pid){
		//delete answer
		String hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)" ;
		answerDao.batchEntityByHQL(hql,pid);
		//delete questions
		hql = "delete from Question q where q.page.id = ?" ;
		questionDao.batchEntityByHQL(hql,pid);
		//delete page
		hql = "delete from Page p where p.id = ?" ;
		pageDao.batchEntityByHQL(hql,pid);
	}
	

	/**
	 * 删除survey,同时删除page,问题,答案
	 */
	public void deleteSurvey(Integer sid){
		//delete answers
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
		
		//hibernate在写操作中,不允许两级以上的链接.
		//hql = "delete from Question q where q.page.survey.id = ?" ;
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)" ;
		questionDao.batchEntityByHQL(hql, sid);
		
		//delete page
		hql = "delete from Page p where p.survey.id = ? " ;
		pageDao.batchEntityByHQL(hql, sid);
		
		//delete survey
		hql = "delete from Survey s where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, sid);
	}
	
	/**
	 * 编辑问题
	 */
	public Question getQuestion(Integer qid){
		return questionDao.getEntity(qid);
	}
	
	/**
	 * 清除调查
	 */
	public void clearAnswers(Integer sid){
		String hql = "delete from Answer a where a.surveyId = ?" ;
		answerDao.batchEntityByHQL(hql,sid);
	}
	
	/**
	 * 切换调查状态
	 */
	public void toggleStatus(Integer sid){
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql,!s.isClosed(),sid);
	}
	
	/**
	 * 更新logoPhoto路径
	 */
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?" ;
		surveyDao.batchEntityByHQL(hql, path,sid);
	}

	/**
	 * 移动和复制页面
	 */
	@Override
	public List<Survey> getSurveyWithPages(User user) {
		/*查询调查集合，携带pages*/
		String hql = "from Survey s where s.user.id=?";
		List<Survey> list = surveyDao.findEntityByHQL(hql, user.getId());
		//这是非常关键的一步，主要是为了解除懒加载：关键
		for(Survey s:list){
			s.getPages().size();
		}
		return list;
	}

	/**
	 * 查询所有可用的调查
	 */
	@Override
	public List<Survey> findAllAvailableSurveys() {
		String sql = "from Survey s where s.closed = ?";
		return surveyDao.findEntityByHQL(sql,false);
	}
	
	/**
	 * 执行移动/赋值页列表页面
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		//判断移动/赋值
		if(srcSurvey.getId().equals(targSurvey.getId())){//同一个调查中----移动
			setOrderno(srcPage,targPage,pos);
		}else{//不同调查中的------复制
			//深度复制之前，强行初始化问题集合
			srcPage.getQuestions().size();
			//深度复制：
			Page copyPage = (Page) DataUtil.deeplyCopy(srcPage);
			//设置页面和目标调查关联
			copyPage.setSurvey(targSurvey);
			//保存页面
			pageDao.saveEntity(copyPage);
			for(Question q : copyPage.getQuestions()){
				questionDao.saveEntity(q);
			}
			setOrderno(srcPage,targPage,pos);
		}
		
	}
/**
 * 设置页序
 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//之前
		if(pos == 0){
			//是否是首页
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}else{
				//取得目标页的前一页
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((targPage.getOrderno()+prePage.getOrderno())/2);
			}
		}else{//之后
			//是否是尾页   
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}else{
				//取得目标页的后一页
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((targPage.getOrderno()+nextPage.getOrderno())/2);
			}
		}
	}
	
	/**
	 * 查询指定页面的下一页
	 */
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ? order by p.orderno asc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * 判断指定页面是否是所在调查尾页
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "select count(*) from Page p where p.survey.id = ? and p.orderno > ? " ;
		Long count = (Long) pageDao.uniqueResult(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return count == 0;
	}

	/**
	 * 查询指定页面的上一页
	 */
	private Page getPrePage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ? order by p.orderno desc" ;
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * 判断指定页面是否是所在调查首页
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "select count(*) from Page p where p.survey.id = ? and p.orderno < ? " ;
		Long count = (Long) pageDao.uniqueResult(hql, targPage.getSurvey().getId(),targPage.getOrderno());
		return count == 0;
	}

	public Page getFirstPage(Integer sid) {
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc";
		List<Page> list = pageDao.findEntityByHQL(hql, sid);
		Page p = list.get(0);
		p.getQuestions().size();
		p.getSurvey().getTitle();//上级初始化，避免懒加载
		return p;
	}

	/**
	 * 获得上一页
	 */
	@Override
	public Page getPrePage(Integer currPid) {
		Page p = this.getPage(currPid);
		p =  this.getPrePage(p);
		p.getQuestions().size();
		return p;
	}

	/**
	 * 获得下一页
	 */
	@Override
	public Page getNextPage(Integer currPid) {
		Page p = this.getPage(currPid);		
		p = this.getNextPage(p);
		p.getQuestions().size();
		return p;
	}

	/**
	 * 批量保存答案
	 */
	@Override
	public void saveAnswers(List<Answer> list) {
		Date date = new Date();
		String uuid = UUID.randomUUID().toString();
		for(Answer a : list){
			//关键注意：这里设置时间和uuid的原因
			a.setUuid(uuid);
			a.setAnswerTime(date);
			answerDao.saveEntity(a);
		}
	}

	/**
	 * 查询指定调查的所有问题：注意：三联查询
	 */
	@Override
	public List<Question> getQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ?" ;
		return questionDao.findEntityByHQL(hql, sid);
	}

	/**
	 * 查询指定调查的所有答案
	 */
	@Override
	public List<Answer> getAnswers(Integer sid) {
		String hql = "from Answer a where a.surveyId = ? order by a.uuid asc";
		return answerDao.findEntityByHQL(hql, sid);
	}
}
