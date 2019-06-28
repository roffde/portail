package fr.deroffal.portail.api.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.deroffal.portail.api.logging.LogAspectOrder.LOG_METHOD_NAME;

@Aspect
@Component
public class LogMethodAndArgumentAspect extends AbstractLogingAspect {

	@Autowired
	private AppLoggerFactory appLoggerFactory;

	@Around(TARGET_BASE_PACKAGE)
	public Object logMethodAndArgument(final ProceedingJoinPoint pjp) throws Throwable {
		final Logger logger = appLoggerFactory.getLogger(pjp.getTarget().getClass());
		if (!logger.isDebugEnabled()) {
			return pjp.proceed();
		}
		final Signature signature = pjp.getSignature();
		final String methodName = signature.getName();

		logger.debug(concatMethodNameAndArgument(methodName, pjp.getArgs()));

		final Object returnedValue = pjp.proceed();

		if (isVoidMethod(signature)) {
			logger.debug("Fin de la méthode {}", methodName);
		} else {
			logger.debug("Fin de la méthode {} avec le retour [{}]", methodName, returnedValue);
		}

		return returnedValue;
	}

	private String concatMethodNameAndArgument(final String methodName, final Object[] args) {
		final String joinedArgs = Stream.of(args)
				.map(Object::toString)
				.map(s -> "'" + s + "'")
				.collect(Collectors.joining(", ", "[", "]"));
		return "Appel de la méthode " + methodName + " avec les arguments : " + joinedArgs + "";
	}

	private boolean isVoidMethod(final Signature signature) {
		return Void.class.equals(((MethodSignature) signature).getReturnType());
	}

	@Override
	LogAspectOrder getLogAspectEnum() {
		return LOG_METHOD_NAME;
	}
}