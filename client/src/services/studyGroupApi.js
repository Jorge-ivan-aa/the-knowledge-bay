import axios from 'axios';

const API_URL = '/api/studygroups';

// Helper to get current user for authoring comments (simplified)
const getCurrentUser = () => {
    try {
        const user = JSON.parse(sessionStorage.getItem("user")) || {};
        return {
            authorId: user.id || 'anonymousUser',
            authorName: user.firstName ? `${user.firstName} ${user.lastName || ''}`.trim() : 'Usuario Anónimo'
        };
    } catch (error) {
        console.error("Error retrieving user data:", error);
        return { authorId: 'anonymousUser', authorName: 'Usuario Anónimo' };
    }
};

export const getAllGroups = async () => {
    const response = await axios.get(API_URL);
    return response.data.map(group => ({
        ...group,
        // Construct heroImage on client-side based on interest
        heroImage: `https://placehold.co/600x300/4A5568/FFFFFF?text=${encodeURIComponent(group.interest || 'Grupo')}`
    })); 
};

export const getGroupById = async (groupId) => {
    const response = await axios.get(`${API_URL}/${groupId}`);
    const group = response.data;
    return {
        ...group,
        heroImage: `https://placehold.co/600x300/4A5568/FFFFFF?text=${encodeURIComponent(group.interest || 'Grupo')}`
    };
};

export const getPostsByGroupId = async (groupId, page = 0, size = 10) => {
    const response = await axios.get(`${API_URL}/${groupId}/posts?page=${page}&size=${size}`);
    return response.data;
};

export const likePost = async (groupId, postId) => {
    // In a real app, userId would come from auth context or secure storage
    const currentUser = getCurrentUser();
    const response = await axios.post(`${API_URL}/${groupId}/posts/${postId}/like`, { userId: currentUser.authorId });
    return response.data;
};

export const addCommentToPost = async (groupId, postId, text) => {
    const currentUser = getCurrentUser();
    const commentData = { 
        text,
        authorId: currentUser.authorId,
        authorName: currentUser.authorName
    };
    const response = await axios.post(`${API_URL}/${groupId}/posts/${postId}/comments`, commentData);
    return response.data;
};

// Placeholder: Create Post (Not implemented due to student restrictions)
// export const createPost = async (groupId, postData) => {
//     const response = await axios.post(`${API_URL}/${groupId}/posts`, postData);
//     return response.data;
// };

// Placeholder: Create Group (Not implemented due to student restrictions)
// export const createStudyGroup = async (groupData) => {
//     const response = await axios.post(API_URL, groupData);
//     return response.data.map(group => ({ ...group, heroImage: `https://placehold.co/600x300/...`}));
// }; 