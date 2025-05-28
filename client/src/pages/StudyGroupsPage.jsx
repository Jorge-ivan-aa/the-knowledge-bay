import React, { useState, useEffect } from "react";
import NavigationBar from "../components/layout/NavigationBar";
import StudyGroupsList from "../components/study-groups/StudyGroupsList";
import GroupView from "../components/study-groups/GroupView";
import {
  getAllGroups,
  getPostsByGroupId,
  likePost,
  addCommentToPost,
} from "../services/studyGroupApi"; // Updated import path

const StudyGroupsPage = () => {
  const [groups, setGroups] = useState([]);
  const [allPosts, setAllPosts] = useState({}); // Store all posts fetched per group
  const [currentGroupPosts, setCurrentGroupPosts] = useState([]); // Posts for the selected group
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [isLoadingPosts, setIsLoadingPosts] = useState(false);
  const [postsPage, setPostsPage] = useState(0); // For pagination
  const [hasMorePosts, setHasMorePosts] = useState(true);

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        setIsLoading(true);
        const fetchedGroups = await getAllGroups();
        setGroups(fetchedGroups);
      } catch (error) {
        console.error("Error fetching groups:", error);
        // Handle error (e.g., show error message to user)
      } finally {
        setIsLoading(false);
      }
    };
    fetchGroups();
  }, []);

  const fetchPostsForGroup = async (groupId, page = 0, loadMore = false) => {
    if (isLoadingPosts && loadMore) return; // Prevent multiple fetches if already loading
    
    setIsLoadingPosts(true);
    try {
      const fetchedPosts = await getPostsByGroupId(groupId, page);
      if (fetchedPosts.length > 0) {
        setAllPosts(prev => ({
            ...prev,
            [groupId]: loadMore ? [...(prev[groupId] || []), ...fetchedPosts] : fetchedPosts
        }));
        setCurrentGroupPosts(loadMore ? [...currentGroupPosts, ...fetchedPosts] : fetchedPosts);
        setPostsPage(page);
        setHasMorePosts(fetchedPosts.length === 10); // Assuming page size is 10
      } else {
        if (!loadMore) { // If initial fetch returns no posts
            setAllPosts(prev => ({ ...prev, [groupId]: [] }));
            setCurrentGroupPosts([]);
        }
        setHasMorePosts(false);
      }
    } catch (error) {
      console.error(`Error fetching posts for group ${groupId}:`, error);
      setHasMorePosts(false); // Stop trying to load more if an error occurs
    } finally {
      setIsLoadingPosts(false);
    }
  };

  const handleSelectGroup = (group) => {
    setSelectedGroup(group);
    setCurrentGroupPosts([]); // Clear previous group's posts
    setPostsPage(0);        // Reset page for new group
    setHasMorePosts(true);  // Assume new group has posts
    if (group) {
        if (allPosts[group.id]) { // If posts are already cached
            setCurrentGroupPosts(allPosts[group.id]);
            // We might want to check if these cached posts are outdated or fetch new ones
            // For simplicity, using cached. For a real app, consider re-fetching or checking freshness.
            setHasMorePosts(allPosts[group.id].length >= 10); // Rough check, depends on if full list was ever fetched
        } else {
            fetchPostsForGroup(group.id, 0); 
        }
    }
  };

  const handleGoBack = () => {
    setSelectedGroup(null);
    setSearchTerm("");
    setCurrentGroupPosts([]);
  };

  const handleLikePost = async (groupId, postId) => {
    try {
      const updatedPost = await likePost(groupId, postId);
      const updatePostInState = (postsList) => 
        postsList.map((post) => (post.id === postId ? updatedPost : post));
      
      setAllPosts(prev => ({
          ...prev,
          [groupId]: updatePostInState(prev[groupId] || [])
      }));
      setCurrentGroupPosts(prevPosts => updatePostInState(prevPosts));

    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleAddComment = async (groupId, postId, commentText) => {
    if (!commentText.trim()) return;
    try {
      const newComment = await addCommentToPost(groupId, postId, commentText);
      const updatePostInState = (postsList) => postsList.map((post) => {
        if (post.id === postId) {
          return { ...post, comments: [...(post.comments || []), newComment] };
        }
        return post;
      });
      
      setAllPosts(prev => ({
        ...prev,
        [groupId]: updatePostInState(prev[groupId] || [])
      }));
      setCurrentGroupPosts(prevPosts => updatePostInState(prevPosts));

    } catch (error) {
      console.error("Error adding comment:", error);
    }
  };

  const fetchMoreGroupPosts = () => {
    if (selectedGroup && hasMorePosts && !isLoadingPosts) {
      fetchPostsForGroup(selectedGroup.id, postsPage + 1, true);
    }
  };

  const filteredGroups = groups.filter(
    (group) =>
      (group.name && group.name.toLowerCase().includes(searchTerm.toLowerCase())) ||
      (group.interest && group.interest.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (isLoading) {
    // You might want a more sophisticated loading screen here
    return (
        <div className="flex flex-col min-h-screen bg-white">
            <NavigationBar title="Grupos de Estudio" />
            <div className="flex-grow flex items-center justify-center">
                <p>Cargando grupos...</p>
            </div>
        </div>
    );
  }

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <NavigationBar title="Grupos de Estudio" />
      
      <div className="flex-grow">
        {selectedGroup ? (
          <GroupView
            group={selectedGroup}
            posts={currentGroupPosts} // Pass current group's posts
            onBack={handleGoBack}
            onLike={handleLikePost}
            onComment={handleAddComment}
            fetchMore={fetchMoreGroupPosts} // Pass the new fetchMore function
            hasMore={hasMorePosts}
            isLoadingMore={isLoadingPosts}
          />
        ) : (
          <StudyGroupsList
            groups={filteredGroups}
            onSelectGroup={handleSelectGroup}
            searchTerm={searchTerm}
            onSearchChange={(e) => setSearchTerm(e.target.value)}
          />
        )}
      </div>

      <footer className="w-full py-6 bg-[var(--deep-sea)] text-white">
        <div className="container mx-auto text-center">
          <p>The Knowledge Bay - Una red social académica.</p>
        </div>
      </footer>
    </div>
  );
};

export default StudyGroupsPage; 