import React, { useState, useEffect } from "react";
import NavigationBar from "../components/layout/NavigationBar";
import StudyGroupsList from "../components/study-groups/StudyGroupsList";
import GroupView from "../components/study-groups/GroupView";

// This would typically come from an API
import { initialGroups, initialPosts } from "../data/studyGroupsData";

const StudyGroupsPage = () => {
  const [groups, setGroups] = useState([]);
  const [posts, setPosts] = useState({});
  const [selectedGroup, setSelectedGroup] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    // Fetch groups and posts from API
    setGroups(initialGroups);
    setPosts(initialPosts);
  }, []);

  const handleSelectGroup = (group) => {
    setSelectedGroup(group);
  };

  const handleGoBack = () => {
    setSelectedGroup(null);
    setSearchTerm("");
  };

  const handleLikePost = (groupId, postId) => {
    setPosts((prevPosts) => {
      const groupPosts = (prevPosts[groupId] || []).map((post) => {
        if (post.id === postId) {
          return {
            ...post,
            likes: post.likedByMe ? post.likes - 1 : post.likes + 1,
            likedByMe: !post.likedByMe,
          };
        }
        return post;
      });
      return { ...prevPosts, [groupId]: groupPosts };
    });
  };

  const handleAddComment = (groupId, postId, commentText) => {
    if (!commentText.trim()) return;
    
    // Get user info from session
    let currentUser = { name: "Usuario" };
    try {
      const user = JSON.parse(sessionStorage.getItem("user")) || {};
      if (user.firstName) {
        currentUser.name = user.firstName + " " + (user.lastName || "");
      }
    } catch (error) {
      console.error("Error retrieving user data:", error);
    }

    setPosts((prevPosts) => {
      const groupPosts = (prevPosts[groupId] || []).map((post) => {
        if (post.id === postId) {
          const newComment = {
            id: `c${Date.now()}`,
            author: { name: currentUser.name },
            text: commentText,
            timestamp: "Ahora mismo",
          };
          return { ...post, comments: [...(post.comments || []), newComment] };
        }
        return post;
      });
      return { ...prevPosts, [groupId]: groupPosts };
    });
  };

  const fetchMorePosts = (groupId) => {
    return new Promise((resolve) => {
      console.log(`Fetching more posts for group ${groupId}`);
      setTimeout(() => {
        // In a real app, this would be an API call
        const groupPosts = posts[groupId] || [];
        const newPosts = [
          {
            id: `post${Date.now()}`,
            type: "markdown",
            author: { name: "Usuario Nuevo" },
            content:
              "Este es un post cargado dinámicamente para el scroll infinito.",
            timestamp: "Ahora mismo",
            likes: 0,
            comments: [],
          },
        ];

        if (newPosts.length > 0) {
          setPosts((prevPosts) => ({
            ...prevPosts,
            [groupId]: [...groupPosts, ...newPosts],
          }));
        }
        resolve();
      }, 1500);
    });
  };

  const filteredGroups = groups.filter(
    (group) =>
      group.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      group.interest.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <NavigationBar title="Grupos de Estudio" />
      
      <div className="flex-grow">
        {selectedGroup ? (
          <GroupView
            group={selectedGroup}
            posts={posts[selectedGroup.id] || []}
            onBack={handleGoBack}
            onLike={handleLikePost}
            onComment={handleAddComment}
            fetchMore={() => fetchMorePosts(selectedGroup.id)}
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