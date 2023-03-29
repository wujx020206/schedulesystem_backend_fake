package cn.edu.fc.javaee.core.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointCuts {

    @Pointcut("execution(public cn.edu.fc.javaee.core.model.ReturnObject cn.edu.xmu..controller..*.*(..))")
    public void controllers() {
    }

    @Pointcut("execution(public * cn.edu.fc..dao..*.*(..))")
    public void daos(){

    }

    @Pointcut("@annotation(cn.edu.fc.javaee.core.aop.Audit)")
    public void auditAnnotation() {
    }
}
