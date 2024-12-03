package kr.pknu.SonYoungHo201911958.dto;

import java.util.List;

public class PostResponse {
    private boolean isSuccess;
    private String code;
    private String message;
    private Result result;

    public static class Result {
        private List<Post> postList;
        private int listSize;
        private boolean hasNext;

        public List<Post> getPostList() {
            return postList;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public int getListSize() {
            return listSize;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setPostList(List<Post> postList) {
            this.postList = postList;
        }

        public void setListSize(int listSize) {
            this.listSize = listSize;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }
    }

    public static class Post {
        private PostInfo postInfo;
        private MemberInfo memberInfo;

        public Post(PostInfo postInfo, MemberInfo memberInfo) {
            this.postInfo = postInfo;
            this.memberInfo = memberInfo;
        }

        public PostInfo getPostInfo() {
            return postInfo;
        }

        public MemberInfo getMemberInfo() {
            return memberInfo;
        }

        public void setPostInfo(PostInfo postInfo) {
            this.postInfo = postInfo;
        }

        public void setMemberInfo(MemberInfo memberInfo) {
            this.memberInfo = memberInfo;
        }
    }

    public static class PostInfo {
        private int postId;
        private String content;
        private String createdAt;
        private int likeCount;
        private boolean likeClickable;

        public PostInfo(int postId, String content, String createdAt, int likeCount, boolean likeClickable) {
            this.postId = postId;
            this.content = content;
            this.createdAt = createdAt;
            this.likeCount = likeCount;
            this.likeClickable = likeClickable;
        }

        public String getContent() {
            return content;
        }

        public int getPostId() {
            return postId;
        }

        public boolean isLikeClickable() {
            return likeClickable;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeClickable(boolean likeClickable) {
            this.likeClickable = likeClickable;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setPostId(int postId) {
            this.postId = postId;
        }
    }

    public static class MemberInfo {
        private String memberName;
        private String profileImageUrl;
        private String sensitivity;
        private String city;
        private String street;

        public MemberInfo(String memberName, String profileImageUrl, String sensitivity, String city, String street) {
            this.memberName = memberName;
            this.profileImageUrl = profileImageUrl;
            this.sensitivity = sensitivity;
            this.city = city;
            this.street = street;
        }

        public String getMemberName() {
            return memberName;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public void setSensitivity(String sensitivity) {
            this.sensitivity = sensitivity;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getSensitivity() {
            return sensitivity;
        }

        public String getCity() {
            return city;
        }

        public String getStreet() {
            return street;
        }
    }

    public void addPost(Post post) {
        if (result == null) {
            result = new Result();
        }
        result.getPostList().add(post);
    }

    public Result getResult() {
        return result;
    }
}
