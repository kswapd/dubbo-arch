package applications;

import commons.AnnotationMapper;
import models.Bar;

/**
 * Created by kongxiangwen on 5/30/18 w:22.
 */
public class main {
	public static void main(String args[]){
		//Bar bar = new Bar();
		//System.out.println(bar.getName());
		Bar bar = null;
		AnnotationMapper mapper = new AnnotationMapper();
		bar = mapper.toPOJO(Bar.class);



		System.out.println(bar.getName());






	}
}
