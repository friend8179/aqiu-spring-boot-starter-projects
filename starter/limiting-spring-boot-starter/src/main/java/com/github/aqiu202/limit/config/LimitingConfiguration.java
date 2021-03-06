package com.github.aqiu202.limit.config;


import com.github.aqiu202.aop.keygen.KeyGenerator;
import com.github.aqiu202.aop.keygen.impl.MethodKeyGenerator;
import com.github.aqiu202.aop.pointcut.AnnotationPointcutAdvisor;
import com.github.aqiu202.limit.anno.CurrentLimiting;
import com.github.aqiu202.limit.anno.RepeatLimiting;
import com.github.aqiu202.limit.anno.ThreadLimiting;
import com.github.aqiu202.limit.aop.CurrentMethodInterceptor;
import com.github.aqiu202.limit.aop.RepeatMethodInterceptor;
import com.github.aqiu202.limit.aop.ThreadMethodInterceptor;
import com.github.aqiu202.limit.key.SessionMethodKeyGenerator;
import com.github.aqiu202.lock.base.CacheLock;
import com.github.aqiu202.util.spel.EvaluationFiller;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LimitingConfiguration {

    public static final String REPEAT_LIMITING_BEAN_NAME = "repeatLimitingBean";
    public static final String THREAD_LIMITING_BEAN_NAME = "threadLimitingBean";
    public static final String CURRENT_LIMITING_BEAN_NAME = "currentLimitingBean";

    public static final String SESSION_METHOD_KEY_GENERATOR_NAME = "sessionMethodKeyGenerator";

    @Bean(REPEAT_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = REPEAT_LIMITING_BEAN_NAME)
    public Advisor repeatLimitingBean(CacheLock cacheLock,
            @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new AnnotationPointcutAdvisor<>(RepeatLimiting.class,
                new RepeatMethodInterceptor(cacheLock, evaluationFiller));
    }

    @Bean(THREAD_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = THREAD_LIMITING_BEAN_NAME)
    public Advisor threadLimitingBean(
            @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new AnnotationPointcutAdvisor<>(ThreadLimiting.class,
                new ThreadMethodInterceptor(evaluationFiller));
    }

    @Bean(CURRENT_LIMITING_BEAN_NAME)
    @ConditionalOnMissingBean(name = CURRENT_LIMITING_BEAN_NAME)
    public Advisor currentLimitingBean(
            @Autowired(required = false) EvaluationFiller evaluationFiller) {
        return new AnnotationPointcutAdvisor<>(CurrentLimiting.class,
                new CurrentMethodInterceptor(evaluationFiller));
    }

    @Bean(SESSION_METHOD_KEY_GENERATOR_NAME)
    @ConditionalOnMissingBean(name = SESSION_METHOD_KEY_GENERATOR_NAME)
    public KeyGenerator sessionMethodKeyGenerator() {
        return new SessionMethodKeyGenerator();
    }


}
