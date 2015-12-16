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
		return possibleAncestor.apply(new DescendantSeeker(), possibleDescendant);
	}
	
	private static class DescendantSeeker extends QuestionAnswerAdaptor<INode,Boolean> {
		
		@Override
		public Boolean defaultINode(INode node, INode question) throws AnalysisException {
			if(node == question) {
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
		
		private Boolean isCorrectChild(INode childNode, INode question) {
			try {
				return childNode.apply(this, question);
			} catch (AnalysisException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		public Boolean createNewReturnValue(INode node, INode question)
				throws org.overture.codegen.cgast.analysis.AnalysisException {
			return false;
		}

		@Override
		public Boolean createNewReturnValue(Object node, INode question)
				throws org.overture.codegen.cgast.analysis.AnalysisException {
			return false;
		}
	}
	
	public static boolean hasDescendantOfType(INode ancestor, Class<? extends INode> type) throws AnalysisException {
		return ancestor.apply(new TypeSeeker(), type);
	}
	
	private static class TypeSeeker extends QuestionAnswerAdaptor<Class<? extends INode>,Boolean> {
		
		@Override
		public Boolean defaultINode(INode node, Class<? extends INode> question) throws AnalysisException {
			if(node.getClass().equals(question)) {
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
		
		private Boolean isCorrectChild(INode childNode, Class<? extends INode> question) {
			try {
				return childNode.apply(this, question);
			} catch (AnalysisException e) {
				e.printStackTrace();
				return false;
			}
		}
		

		@Override
		public Boolean createNewReturnValue(INode node, Class<? extends INode> question) throws AnalysisException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Boolean createNewReturnValue(Object node, Class<? extends INode> question) throws AnalysisException {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
