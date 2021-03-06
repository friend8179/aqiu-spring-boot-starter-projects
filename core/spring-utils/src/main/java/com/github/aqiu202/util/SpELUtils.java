package com.github.aqiu202.util;

import com.github.aqiu202.util.spel.EvaluationFiller;
import java.lang.reflect.Method;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * <pre>SpELUtils</pre>
 *
 * @author aqiu 2020/11/29 1:23
 **/
public abstract class SpELUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static boolean isSpEL(String value) {
        return value.replace("\\#", "").contains("#");
    }

    public static String handleSpEl(String key, Object target, Method method, Object[] parameters,
            EvaluationFiller evaluationFiller) {
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(null,
                method, parameters, parameterNameDiscoverer);
        context.setVariable("_method",
                target.getClass().getName().concat(".").concat(method.getName()));
        if (evaluationFiller != null) {
            evaluationFiller.fill(context, target, method, parameters);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

    public static String handleSpEl(String key, Object target, Method method, Object[] parameters) {
        return handleSpEl(key, target, method, parameters, null);
    }

    public static String handleNormalKey(String key) {
        return key.replace("\\#", "#");
    }

}
