package org.openscience.cdk.hash.stereo;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openscience.cdk.interfaces.ITetrahedralChirality.Stereo.CLOCKWISE;

/**
 * Defines a stereo encoder factory for the hash code. The factory allows the 
 * generation of stereo hash codes for molecules with predefined
 * {@link ITetrahedralChirality} stereo elements.
 * 
 * @author John May
 * @cdk.module hash
 */
@TestClass("org.openscience.cdk.hash.stereo.TetrahedralElementEncoderFactoryTest")
public final class TetrahedralElementEncoderFactory implements StereoEncoderFactory {

    /**
     * @inheritDoc
     */
    @TestMethod("createExplicitH,createImplicitH_back,createImplicitH_front")
    @Override public StereoEncoder create(IAtomContainer container, int[][] graph) {

        // index atoms for quick lookup - wish we didn't have to do this
        // but the it's better than calling getAtomNumber every time - we use
        // a lazy creation so it's only created if there was a need for it
        Map<IAtom, Integer> atomToIndex = null;

        List<StereoEncoder> encoders = new ArrayList<StereoEncoder>();

        // for each tetrahedral element - create a new encoder
        for (IStereoElement se : container.stereoElements()) {
            if (se instanceof ITetrahedralChirality) {
                encoders.add(encoder((ITetrahedralChirality) se,
                                     atomToIndex = indexMap(atomToIndex, container)));
            }
        }

        return encoders.isEmpty() ? StereoEncoder.EMPTY 
                                  : new MultiStereoEncoder(encoders);
    }

    /**
     * Create an encoder for the {@link ITetrahedralChirality} element.
     *
     * @param tc          stereo element from an atom container
     * @param atomToIndex map of atoms to indices
     * @return a new geometry encoder
     */
    private static GeometryEncoder encoder(ITetrahedralChirality tc,
                                           Map<IAtom,Integer> atomToIndex) {

        IAtom[] ligands = tc.getLigands();
        
        int     centre  = atomToIndex.get(tc.getChiralAtom());
        int[]   indices = new int[4];

        int offset = -1;

        for (int i = 0; i < ligands.length; i++) {
            indices[i] = atomToIndex.get(ligands[i]);
            if (indices[i] == centre)
                offset = i;            
        }

        // convert clockwise/anticlockwise to -1/+1
        int parity = tc.getStereo() == CLOCKWISE ? -1 : 1;

        // now if any atom is the centre (indicating an implicit
        // hydrogen) we need to adjust the indicies and the parity 
        if (offset >= 0) {

            // remove the 'implicit' central from the first 3 vertices 
            for (int i = offset; i < indices.length - 1; i++) {
                indices[i] = indices[i + 1];
            }

            // we now take how many vertices we moved which is
            // 3 (last index) minus the index where we started. if the
            // value is odd we invert the parity (odd number of
            // inversions)
            if (Integer.lowestOneBit(3 - offset) == 0x1)
                parity *= -1;

            // trim the array to size we don't include the last (implicit)
            // vertex when checking the invariants                    
            indices = Arrays.copyOf(indices, indices.length - 1);
        }
        
        return new GeometryEncoder(centre,
                                   new BasicPermutationParity(indices),
                                   GeometricParity.valueOf(parity));
    }

    /**
     * Lazy creation of an atom index map.
     *
     * @param map       existing map (possibly null)
     * @param container the container we want the map for
     * @return a usable atom to index map for the given container
     */
    private static Map<IAtom, Integer> indexMap(Map<IAtom, Integer> map, IAtomContainer container) {
        if (map != null)
            return map;
        map = new HashMap<IAtom, Integer>();
        for (IAtom a : container.atoms()) {
            map.put(a, map.size());
        }
        return map;
    }
}
