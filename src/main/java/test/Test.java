package test;

import org.springframework.stereotype.Component;

@Component("mytest")
public class Test {
	public void print() {
		System.out.println(this.getClass().getCanonicalName());
	}
}
