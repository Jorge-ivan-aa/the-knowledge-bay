import React, { useState, useEffect, useRef } from "react";
import { ChevronLeft } from "lucide-react";
import PostItem from "./PostItem";
import LoadingSpinner from "../common/LoadingSpinner";

const GroupView = ({
  group,
  posts,
  onBack,
  onLike,
  onComment,
  fetchMore,
}) => {
  const [commentTexts, setCommentTexts] = useState({});
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const scrollableContainerRef = useRef(null);

  const handleCommentChange = (postId, text) => {
    setCommentTexts((prev) => ({ ...prev, [postId]: text }));
  };

  const submitComment = (postId) => {
    onComment(group.id, postId, commentTexts[postId] || "");
    setCommentTexts((prev) => ({ ...prev, [postId]: "" }));
  };

  useEffect(() => {
    const container = scrollableContainerRef.current;
    if (!container) return;

    const handleScroll = () => {
      const { scrollTop, scrollHeight, clientHeight } = container;
      if (scrollHeight - scrollTop - clientHeight < 1 && hasMore && !isLoadingMore) {
        setIsLoadingMore(true);
        fetchMore()
          .then(() => {
            setIsLoadingMore(false);
          })
          .catch(() => {
            setIsLoadingMore(false);
            setHasMore(false);
          });
      }
    };

    container.addEventListener("scroll", handleScroll);
    return () => container.removeEventListener("scroll", handleScroll);
  }, [fetchMore, hasMore, isLoadingMore]);

  return (
    <div className="flex flex-col min-h-screen bg-white">
      <header className="p-4 sm:p-6 sticky top-0 bg-white shadow-md z-20">
        <div className="container mx-auto flex items-center justify-between">
          <button
            onClick={onBack}
            className="flex items-center text-[var(--coastal-sea)] hover:text-[var(--open-sea)] transition-colors p-2 rounded-md hover:bg-gray-100"
          >
            <ChevronLeft size={28} className="mr-1" />
            <span className="hidden sm:inline">Volver a Grupos</span>
          </button>
          <div className="text-center">
            <h1 className="text-xl sm:text-2xl font-bold text-[var(--deep-sea)]">
              {group.name}
            </h1>
            <p className="text-xs sm:text-sm text-gray-500">
              Interés: {group.interest}
            </p>
          </div>
          <div className="w-10"></div> {/* Empty div for spacing balance */}
        </div>
      </header>

      <main
        ref={scrollableContainerRef}
        className="container mx-auto p-4 sm:p-6 flex-grow overflow-y-auto"
      >
        <div className="space-y-6">
          {posts.map((post) => (
            <PostItem
              key={post.id}
              post={post}
              onLike={() => onLike(group.id, post.id)}
              onCommentSubmit={() => submitComment(post.id)}
              commentText={commentTexts[post.id] || ""}
              onCommentChange={(text) => handleCommentChange(post.id, text)}
            />
          ))}
        </div>
        {isLoadingMore && <LoadingSpinner />}
        {!hasMore && posts.length > 0 && (
          <p className="text-center text-gray-500 my-8">
            ¡Has llegado al final!
          </p>
        )}
        {!posts.length && !isLoadingMore && (
          <p className="text-center text-gray-500 text-lg mt-10">
            No hay publicaciones en este grupo todavía.
          </p>
        )}
      </main>
    </div>
  );
};

export default GroupView; 