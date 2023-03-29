//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.fc.javaee.core.aop;

import cn.edu.fc.javaee.core.model.ReturnNo;
import cn.edu.fc.javaee.core.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DaoAspect {

    private final Logger logger = LoggerFactory.getLogger(DaoAspect.class);

    @Around("cn.edu.fc.javaee.core.aop.CommonPointCuts.daos()")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        Object obj = null;

        MethodSignature ms = (MethodSignature) jp.getSignature();
        Object target = jp.getTarget();

        try {
            obj = jp.proceed();
            logger.debug("doAround: obj = {}, method = {}", target, ms.getName());
        } catch(BusinessException e){
            throw e;
        }
        catch (Exception exception) {

            logger.error("doAround: obj = {}, method = {}, e = {}", target, ms.getName(), exception);
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, exception.getMessage());
        }
        return obj;
    }

}
