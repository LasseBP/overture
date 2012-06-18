package org.overture.interpreter.assistant.pattern;

import java.util.List;
import java.util.Vector;

import org.overture.ast.patterns.AQuotePattern;
import org.overture.interpreter.runtime.Context;
import org.overture.interpreter.runtime.PatternMatchException;
import org.overture.interpreter.runtime.RuntimeError;
import org.overture.interpreter.runtime.ValueException;
import org.overture.interpreter.values.NameValuePairList;
import org.overture.interpreter.values.Value;
import org.overture.typechecker.assistant.pattern.AQuotePatternAssistantTC;

public class AQuotePatternAssistantInterpreter extends AQuotePatternAssistantTC
{

	public static List<NameValuePairList> getAllNamedValues(AQuotePattern p,
			Value expval, Context ctxt) throws PatternMatchException
	{
		List<NameValuePairList> result = new Vector<NameValuePairList>();

		try
		{
			if (!expval.quoteValue(ctxt).equals(p.getValue().value))
			{
				RuntimeError.patternFail(4112, "Quote pattern match failed",p.getLocation());
			}
		}
		catch (ValueException e)
		{
			RuntimeError.patternFail(e,p.getLocation());
		}

		result.add(new NameValuePairList());
		return result;
	}

}
