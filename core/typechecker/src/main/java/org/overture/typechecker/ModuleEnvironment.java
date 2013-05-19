/*******************************************************************************
 *
 *	Copyright (c) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overture.typechecker;

import java.util.List;
import java.util.Set;

import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.intf.lex.ILexIdentifierToken;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.typechecker.NameScope;
import org.overture.typechecker.assistant.definition.PDefinitionAssistantTC;
import org.overture.typechecker.assistant.definition.PDefinitionListAssistantTC;




/**
 * Define the type checking environment for a modular specification.
 */

public class ModuleEnvironment extends Environment
{
	private final AModuleModules module;

	public List<PDefinition> getDefinitions()
	{
		return module.getDefs();
	}
	
	public ModuleEnvironment(AModuleModules module)
	{
		super(null,null);
		this.module = module;
		dupHideCheck(module.getDefs(), NameScope.NAMESANDSTATE);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (PDefinition d: module.getDefs())
		{
			sb.append(d.getName().getFullName());
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public PDefinition findName(ILexNameToken name, NameScope scope)
	{
		PDefinition def = PDefinitionListAssistantTC.findName(module.getDefs(),name, scope);

		if (def != null)
		{
			return def;
		}

		def = PDefinitionListAssistantTC.findName(module.getImportdefs(),name, scope);

		if (def != null)
		{
			return def;
		}

   		return null;	// Modules are always bottom of the env chain
	}

	@Override
	public PDefinition findType(ILexNameToken name, String fromModule)
	{
		PDefinition def = PDefinitionAssistantTC.findType(module.getDefs(), name,module.getName().getName());

		if (def != null)
		{
			return def;
		}

		def =  PDefinitionAssistantTC.findType(module.getImportdefs(),name,module.getName().getName());

		if (def != null)
		{
			return def;
		}

		return null;	// Modules are always bottom of the env chain
	}

	@Override
	public Set<PDefinition> findMatches(ILexNameToken name)
	{
		Set<PDefinition> defs = PDefinitionListAssistantTC.findMatches(module.getDefs(),name);
		defs.addAll(PDefinitionListAssistantTC.findMatches(module.getImportdefs(),name));
		return defs;
	}

	@Override
	public void unusedCheck()
	{
		// The usage of all modules is checked at the end of the type check
		// phase. Only flat environments implement this check, for unused
		// local definitions introduced by expressions and statements.
	}

	@Override
	public AStateDefinition findStateDefinition()
	{
		AStateDefinition def = PDefinitionListAssistantTC.findStateDefinition(module.getDefs());

		if (def != null)
		{
			return def;
		}

		return null;	// Modules are always bottom of the env chain
	}

	@Override
	public boolean isVDMPP()
	{
		return false;
	}

	@Override
	public boolean isSystem()
	{
		return false;
	}

	@Override
	public SClassDefinition findClassDefinition()
	{
		return null;
	}

	@Override
	public boolean isStatic()
	{
		return false;
	}

	@Override
	public PDefinition find(ILexIdentifierToken name) {
		return null;
	}
}
