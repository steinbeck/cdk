/* ConnectivityCheckerTest.java
 *
 * $RCSfile$    $Author$    $Date$    $Revision$
 * 
 * Copyright (C) 1997-2000  The CompChem project
 * 
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 * 
 */

package org.openscience.cdk.test;

import org.openscience.cdk.*;
import org.openscience.cdk.tools.*;
import java.util.*;

/**
 * Checks the funcitonality of the ConnectivityChecker
 */
 
public class ConnectivityCheckerTest
{

	public ConnectivityCheckerTest()
	{
		Molecule mol;
		AtomContainer atomCon = new AtomContainer();
		Vector molecules = null;
		ConnectivityChecker cc = new ConnectivityChecker();
		mol = buildMolecule4x3();
		atomCon.add(mol);
		mol = buildConnectedMolecule();
		atomCon.add(mol);
		mol = buildSpiroRings();
		atomCon.add(mol);
//		System.out.println(cc.isConnected(mol));

		try
		{
			molecules = cc.partitionIntoMolecules(atomCon);
			System.out.println("number of molecules found  "+molecules.size());
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
		}
		for (int i = 0; i < molecules.size(); i++)
		{
			System.out.println("Molecule number "+ i + molecules.elementAt(i));
		}
	}
	

	public static void main(String[] args)
	{
		new ConnectivityCheckerTest();
	}
	
	/* build an a-pinen molecule */
	
	Molecule buildConnectedMolecule()
	{
		Molecule mol = new Molecule();
		mol.addAtom(new Atom("C")); // 1
		mol.addAtom(new Atom("C")); // 2
		mol.addAtom(new Atom("C")); // 3
		mol.addAtom(new Atom("C")); // 4
		mol.addAtom(new Atom("C")); // 5
		mol.addAtom(new Atom("C")); // 6
		mol.addAtom(new Atom("C")); // 7
		mol.addAtom(new Atom("C")); // 8
		mol.addAtom(new Atom("C")); // 9 
		mol.addAtom(new Atom("C")); // 10
		
		mol.addBond(0, 1, 2); // 1
		mol.addBond(1, 2, 1); // 2
		mol.addBond(2, 3, 1); // 3
		mol.addBond(3, 4, 1); // 4
		mol.addBond(4, 5, 1); // 5
		mol.addBond(5, 0, 1); // 6
		mol.addBond(0, 6, 1); // 7
		mol.addBond(3, 7, 1); // 8
		mol.addBond(5, 7, 1); // 9
		mol.addBond(7, 8, 1); // 10
		mol.addBond(7, 9, 1); // 11
		return mol;
	}
	/* build a molecule from 4 condensed triangles */
	
	Molecule buildMolecule4x3()
	{
		Molecule mol = new Molecule();
		mol.addAtom(new Atom("C")); // 1
		mol.addAtom(new Atom("C")); // 2
		mol.addAtom(new Atom("C")); // 3
		mol.addAtom(new Atom("C")); // 4
		mol.addAtom(new Atom("C")); // 5
		mol.addAtom(new Atom("C")); // 6
		mol.addAtom(new Atom("C")); // 7		
		
		mol.addBond(0, 1, 1); // 1
		mol.addBond(1, 2, 1); // 2
		mol.addBond(2, 0, 1); // 3
		mol.addBond(2, 3, 1); // 4
		mol.addBond(1, 3, 1); // 5
		mol.addBond(3, 4, 1); // 6
		mol.addBond(4, 2, 1); // 7
		mol.addBond(4, 5, 1); // 8
		mol.addBond(5, 6, 1); // 9
		mol.addBond(6, 4, 1); // 10		
		
		return mol;
	}
	
	Molecule buildSpiroRings()
	{
		Molecule mol = new Molecule();
		mol.addAtom(new Atom("C")); // 0
		mol.addAtom(new Atom("C")); // 1
		mol.addAtom(new Atom("C")); // 2
		mol.addAtom(new Atom("C")); // 3
		mol.addAtom(new Atom("C")); // 4
		mol.addAtom(new Atom("C")); // 5
		mol.addAtom(new Atom("C")); // 6
		mol.addAtom(new Atom("C")); // 7
		mol.addAtom(new Atom("C")); // 8
		mol.addAtom(new Atom("C")); // 9

		
		
		mol.addBond(0, 1, 1); // 1
		mol.addBond(1, 2, 1); // 2
		mol.addBond(2, 3, 1); // 3
		mol.addBond(3, 4, 1); // 4
		mol.addBond(4, 5, 1); // 5
		mol.addBond(5, 6, 1); // 6
		mol.addBond(6, 0, 1); // 7
		mol.addBond(6, 7, 1); // 8
		mol.addBond(7, 8, 1); // 9
		mol.addBond(8, 9, 1); // 10
		mol.addBond(9, 6, 1); // 11				
		return mol;
	}
	
}