package com.tianfeng.swzn.facegrade.jsonBean;

import java.util.List;

public class FaceResult {

    /**
     * cached : 0
     * error_code : 0
     * error_msg : SUCCESS
     * log_id : 9900120184840
     * result : {"face_list":[{"age":24,"angle":{"pitch":-16.17018127,"roll":-13.28870869,"yaw":4.665333748},"beauty":33.1857338,"face_probability":0.9353134632,"face_token":"98972dbcbd3f9a1b6516e30c750216ea","face_type":{"probability":0.9998556376,"type":"human"},"gender":{"probability":0.9941805601,"type":"male"},"glasses":{"probability":0.9991256595,"type":"common"},"location":{"height":239,"left":169.3163147,"rotation":-14,"top":504.0287781,"width":208},"race":{"probability":0.9980068803,"type":"yellow"}}],"face_num":1}
     * timestamp : 1534472213
     */

    private int cached;
    private int error_code;
    private String error_msg;
    private long log_id;
    private ResultBean result;
    private int timestamp;

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean {
        /**
         * face_list : [{"age":24,"angle":{"pitch":-16.17018127,"roll":-13.28870869,"yaw":4.665333748},"beauty":33.1857338,"face_probability":0.9353134632,"face_token":"98972dbcbd3f9a1b6516e30c750216ea","face_type":{"probability":0.9998556376,"type":"human"},"gender":{"probability":0.9941805601,"type":"male"},"glasses":{"probability":0.9991256595,"type":"common"},"location":{"height":239,"left":169.3163147,"rotation":-14,"top":504.0287781,"width":208},"race":{"probability":0.9980068803,"type":"yellow"}}]
         * face_num : 1
         */

        private int face_num;
        private List<FaceListBean> face_list;

        public int getFace_num() {
            return face_num;
        }

        public void setFace_num(int face_num) {
            this.face_num = face_num;
        }

        public List<FaceListBean> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceListBean> face_list) {
            this.face_list = face_list;
        }

        public static class FaceListBean {
            /**
             * age : 24
             * angle : {"pitch":-16.17018127,"roll":-13.28870869,"yaw":4.665333748}
             * beauty : 33.1857338
             * face_probability : 0.9353134632
             * face_token : 98972dbcbd3f9a1b6516e30c750216ea
             * face_type : {"probability":0.9998556376,"type":"human"}
             * gender : {"probability":0.9941805601,"type":"male"}
             * glasses : {"probability":0.9991256595,"type":"common"}
             * location : {"height":239,"left":169.3163147,"rotation":-14,"top":504.0287781,"width":208}
             * race : {"probability":0.9980068803,"type":"yellow"}
             */

            private int age;
            private AngleBean angle;
            private double beauty;
            private double face_probability;
            private String face_token;
            private FaceTypeBean face_type;
            private GenderBean gender;
            private GlassesBean glasses;
            private LocationBean location;
            private RaceBean race;

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public AngleBean getAngle() {
                return angle;
            }

            public void setAngle(AngleBean angle) {
                this.angle = angle;
            }

            public double getBeauty() {
                return beauty;
            }

            public void setBeauty(double beauty) {
                this.beauty = beauty;
            }

            public double getFace_probability() {
                return face_probability;
            }

            public void setFace_probability(double face_probability) {
                this.face_probability = face_probability;
            }

            public String getFace_token() {
                return face_token;
            }

            public void setFace_token(String face_token) {
                this.face_token = face_token;
            }

            public FaceTypeBean getFace_type() {
                return face_type;
            }

            public void setFace_type(FaceTypeBean face_type) {
                this.face_type = face_type;
            }

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public GlassesBean getGlasses() {
                return glasses;
            }

            public void setGlasses(GlassesBean glasses) {
                this.glasses = glasses;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public RaceBean getRace() {
                return race;
            }

            public void setRace(RaceBean race) {
                this.race = race;
            }

            public static class AngleBean {
                /**
                 * pitch : -16.17018127
                 * roll : -13.28870869
                 * yaw : 4.665333748
                 */

                private double pitch;
                private double roll;
                private double yaw;

                public double getPitch() {
                    return pitch;
                }

                public void setPitch(double pitch) {
                    this.pitch = pitch;
                }

                public double getRoll() {
                    return roll;
                }

                public void setRoll(double roll) {
                    this.roll = roll;
                }

                public double getYaw() {
                    return yaw;
                }

                public void setYaw(double yaw) {
                    this.yaw = yaw;
                }
            }

            public static class FaceTypeBean {
                /**
                 * probability : 0.9998556376
                 * type : human
                 */

                private double probability;
                private String type;

                public double getProbability() {
                    return probability;
                }

                public void setProbability(double probability) {
                    this.probability = probability;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class GenderBean {
                /**
                 * probability : 0.9941805601
                 * type : male
                 */

                private double probability;
                private String type;

                public double getProbability() {
                    return probability;
                }

                public void setProbability(double probability) {
                    this.probability = probability;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class GlassesBean {
                /**
                 * probability : 0.9991256595
                 * type : common
                 */

                private double probability;
                private String type;

                public double getProbability() {
                    return probability;
                }

                public void setProbability(double probability) {
                    this.probability = probability;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class LocationBean {
                /**
                 * height : 239
                 * left : 169.3163147
                 * rotation : -14
                 * top : 504.0287781
                 * width : 208
                 */

                private int height;
                private double left;
                private int rotation;
                private double top;
                private int width;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public double getLeft() {
                    return left;
                }

                public void setLeft(double left) {
                    this.left = left;
                }

                public int getRotation() {
                    return rotation;
                }

                public void setRotation(int rotation) {
                    this.rotation = rotation;
                }

                public double getTop() {
                    return top;
                }

                public void setTop(double top) {
                    this.top = top;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }
            }

            public static class RaceBean {
                /**
                 * probability : 0.9980068803
                 * type : yellow
                 */

                private double probability;
                private String type;

                public double getProbability() {
                    return probability;
                }

                public void setProbability(double probability) {
                    this.probability = probability;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}
