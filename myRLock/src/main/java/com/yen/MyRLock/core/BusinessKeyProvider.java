package com.yen.MyRLock.core;

import com.yen.MyRLock.annotation.MyRLock;
import com.yen.MyRLock.annotation.MyRLockKey;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class BusinessKeyProvider {

  private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

  private final ExpressionParser parser = new SpelExpressionParser();

  public String getKeyName(JoinPoint joinPoint, MyRLock klock) {
    List<String> keyList = new ArrayList<>();
    Method method = getMethod(joinPoint);
    List<String> definitionKeys = getSpelDefinitionKey(klock.keys(), method, joinPoint.getArgs());
    keyList.addAll(definitionKeys);
    List<String> parameterKeys = getParameterKey(method.getParameters(), joinPoint.getArgs());
    keyList.addAll(parameterKeys);
    return StringUtils.collectionToDelimitedString(keyList, "", "-", "");
  }

  private Method getMethod(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    if (method.getDeclaringClass().isInterface()) {
      try {
        method =
            joinPoint
                .getTarget()
                .getClass()
                .getDeclaredMethod(signature.getName(), method.getParameterTypes());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return method;
  }

  private List<String> getSpelDefinitionKey(
      String[] definitionKeys, Method method, Object[] parameterValues) {
    List<String> definitionKeyList = new ArrayList<>();
    for (String definitionKey : definitionKeys) {
      if (!ObjectUtils.isEmpty(definitionKey)) {
        EvaluationContext context =
            new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
        Object objKey = parser.parseExpression(definitionKey).getValue(context);
        definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
      }
    }
    return definitionKeyList;
  }

  private List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
    List<String> parameterKey = new ArrayList<>();
    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i].getAnnotation(MyRLockKey.class) != null) {
        MyRLockKey keyAnnotation = parameters[i].getAnnotation(MyRLockKey.class);
        if (keyAnnotation.value().isEmpty()) {
          Object parameterValue = parameterValues[i];
          parameterKey.add(ObjectUtils.nullSafeToString(parameterValue));
        } else {
          StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
          Object key = parser.parseExpression(keyAnnotation.value()).getValue(context);
          parameterKey.add(ObjectUtils.nullSafeToString(key));
        }
      }
    }
    return parameterKey;
  }
}
