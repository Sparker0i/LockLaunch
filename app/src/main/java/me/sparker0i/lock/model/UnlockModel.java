package me.sparker0i.lock.model;

import java.util.ArrayList;

public class UnlockModel {
    public ArrayList<Images> images;
    public String uploaded_image_url;
    public ArrayList<AddModel.Errors> Errors;

    public class Images {
        public Transaction transaction;
        public ArrayList<Candidates> candidates;
    }

    public class Transaction {
        public String status;
        public int topLeftX;
        public int topLeftY;
        public String gallery_name;
        public String subject_id;
        public float confidence;
        public int eyeDistance;
        public int height;
        public int width;
        public int face_id;
        public float quality;
    }

    public class Candidates {
        public String subject_id;
        public String face_id;
        public float confidence;
        public String enrollment_timestamp;
    }
}
