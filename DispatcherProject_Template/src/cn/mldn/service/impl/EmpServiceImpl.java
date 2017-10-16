package cn.mldn.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mldn.vo.Emp;

public class EmpServiceImpl {
	public Map<String,Object> list(String column,String keyWord,long currentPage,int lineSize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("allRecorders", 99L);//假设有99行记录
		List<Emp> all = new ArrayList<Emp>();
		for(long x=(currentPage - 1)*lineSize;x<currentPage*lineSize;x++) {
			Emp vo = new Emp();
			vo.setEmpno(x);
			vo.setEname("{"+keyWord+"}={"+column+"} - "+x);
			vo.setAge(10);
			all.add(vo);
		}
		map.put("allEmps", all);
		return map;
	}

}
