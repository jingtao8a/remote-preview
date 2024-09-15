package org.jingtao8a.remote_preview.aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jingtao8a.remote_preview.annotation.GlobalInterceptor;
import org.jingtao8a.remote_preview.annotation.VerifyParam;
import org.jingtao8a.remote_preview.enums.ResponseCodeEnum;
import org.jingtao8a.remote_preview.exception.BusinessException;
import org.jingtao8a.remote_preview.utils.VerifyUtils;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    private static final Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);

    private static final String TYPE_STRING = "java.lang.String";
    private static final String TYPE_INTEGER = "java.lang.Integer";
    private static final String TYPE_LONG = "java.lang.Long";

    //    @annnotation(org.jingtao8a.easypan.GlobalInterceptor 切点表达式
    @Pointcut("@annotation(org.jingtao8a.remote_preview.annotation.GlobalInterceptor)")// 统一切点表达式 管理
    private void requestInterceptor() {

    }

    @Before("requestInterceptor()")
    public void interceptorDo(JoinPoint point) throws BusinessException {
        try {
            Object target = point.getTarget();
            Object[] arguments = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] paramterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();

            Method method = target.getClass().getMethod(methodName, paramterTypes);
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (null == interceptor) {
                return;
            }
            //校验参数
            if (interceptor.checkParams()) {
                validateParams(method, arguments);
            }
        } catch (BusinessException e) {
            logger.error("全局异常拦截器", e);
            throw e;
        } catch (Throwable e) {
            logger.error("全局异常拦截器", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }

    private void validateParams(Method method, Object[] arguments) throws BusinessException {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            Parameter parameter = parameters[i];
            Object value = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if (verifyParam == null) {
                continue;
            }
            //基本数据类型
            if (TYPE_STRING.equals(parameter.getParameterizedType().getTypeName()) || TYPE_LONG.equals(parameter.getParameterizedType().getTypeName()) ||
                    TYPE_INTEGER.equals(parameter.getParameterizedType().getTypeName())) {
                checkValue(value, verifyParam);
                //校验对象
            } else {
                checkObjValue(parameter, value);
            }
        }
    }

    private void checkObjValue(Parameter parameter, Object value) throws BusinessException {
        try {
            String typeName = parameter.getParameterizedType().getTypeName();
            Class clazz = Class.forName(typeName);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam fieldVerifyParam = field.getAnnotation(VerifyParam.class);
                if (fieldVerifyParam == null) {
                    continue;
                }
                field.setAccessible(true);
                Object resultValue = field.get(value);
                checkValue(resultValue, fieldVerifyParam);
            }
        } catch (BusinessException e) {
            logger.error("校验参数失败", e);
            throw e;
        } catch (Exception e) {
            logger.error("校验参数失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    private void checkValue(Object value, VerifyParam verifyParam) throws BusinessException {
        Boolean isEmpty = value == null || value.toString().isEmpty();
        Integer length = value == null ? 0 : value.toString().length();
        //校验空
        if (isEmpty && verifyParam.required()) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //校验长度
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length ||
                verifyParam.min() != -1 && verifyParam.min() > length)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //校验正则
        if (!isEmpty && !verifyParam.regex().getRegex().isEmpty() && !VerifyUtils.verify(verifyParam.regex(), String.valueOf(value))) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }
}
