package com.example.musicplayerservice.db;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * MusicPlayerService
 * created by
 *
 * @author gudimov
 * on 09 march 2022
 */
public class FirebaseST {
    private static final String FIREBASE_STORAGE = "gs://fir-connectivitytest-5c9c9.appspot.com";
    private static FirebaseStorage storage;

    /**
     * private empty constructor
     */
    private FirebaseST() {
    }

    /**
     * get Firebase Storage reference
     *
     * @return Firebase Storage reference
     */
    public static StorageReference getStorageRef() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance(FIREBASE_STORAGE);
        }
        return storage.getReference();
    }
}
