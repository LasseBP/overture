/*
 * #%~
 * VDM Code Generator
 * %%
 * Copyright (C) 2008 - 2014 Overture
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #~%
 */
package org.overture.codegen.assistant;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.QuestionAnswerAdaptor;

public abstract class AssistantBase
{
	protected AssistantManager assistantManager;

	public AssistantBase(AssistantManager assistantManager)
	{
		super();
		this.assistantManager = assistantManager;
	}
	
	public static <T extends INode> List<T> cloneNodes(List<T> list, Class<T> nodeType)
	{
		List<T> cloneList = new LinkedList<T>();
		
		for(T e : list)
		{
			Object clone = e.clone();
			
			if(nodeType.isInstance(clone))
			{
				cloneList.add(nodeType.cast(clone));
			}
		}
		
		return cloneList;
	}
	
	
	/**
	 * Determines whether a node(possibleDescendant) is in the subtree of another node (PossibleAncestor).
	 * 
	 * @param possibleAncestor
	 * @param possibleDescendant
	 * @return
	 * @throws AnalysisException
	 */
	public static boolean isDescendant(INode possibleAncestor, INode possibleDescendant) throws AnalysisException {
		return forallDescendants(possibleAncestor, node -> node == possibleDescendant);
	}
	
	public static boolean hasDescendantOfType(INode ancestor, Class<? extends INode> type) throws AnalysisException {
		return forallDescendants(ancestor, node -> node.getClass().equals(type));
	}
	
	public static boolean forallDescendants(INode ancestor, Predicate<INode> predicate) throws AnalysisException {
		return ancestor.apply(new DescendantChecker(), predicate);
	}
	
	private static class DescendantChecker extends QuestionAnswerAdaptor<Predicate<INode>,Boolean> {
		
		@Override
		public Boolean defaultINode(INode node, Predicate<INode> question) throws AnalysisException {
			if(question.test(node)) {
				return true;
			}
			return node.getChildren(true).values()
										 .stream()
										 .filter(child -> child instanceof INode) //only handle INode children
										 .map(child -> (INode)child)
										 .map(childNode -> isCorrectChild(childNode, question)) //look at all child nodes
										 .filter(isCorrectChild -> isCorrectChild) //filter for correct child
										 .findFirst()							   //stop looking when correct child is found 										  
										 .orElse(false);						   //return false if not found
		}
		
		private Boolean isCorrectChild(INode childNode, Predicate<INode> question) {
			try {
				return childNode.apply(this, question);
			} catch (AnalysisException e) {
				//must swallow exception. Throwing checked exceptions during stream eval is illegal.
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		public Boolean createNewReturnValue(INode node, Predicate<INode> question) throws AnalysisException {
			return false;
		}

		@Override
		public Boolean createNewReturnValue(Object node, Predicate<INode> question) throws AnalysisException {
			return false;
		}
	}
}
