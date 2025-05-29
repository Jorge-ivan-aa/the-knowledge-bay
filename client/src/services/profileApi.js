// client/src/services/profileApi.js
import authApi from './authApi';

// Fetch current user profile
export const getProfile = async () => {
  try {
    // Obtener datos del perfil real
    return await authApi.get('/api/profile');
  } catch (error) {
    console.error('Error al obtener el perfil:', error);
    return {
      success: false,
      message: 'No se pudo obtener el perfil. Verifica la conexión y el token de autenticación.'
    };
  }
};

// Update user profile
export const updateProfile = async (profileData) => {
  try {
    // Actualizar el perfil real
    return await authApi.put('/api/profile', profileData);
  } catch (error) {
    console.error('Error al actualizar el perfil:', error);
    return {
      success: false,
      message: 'No se pudo actualizar el perfil. Verifica la conexión y el token de autenticación.'
    };
  }
};

// Fetch other user profile by userId
export const getProfileByUserId = async (userId) => {
  try {
    console.log(`Obteniendo perfil real para el usuario ${userId}`);
    return await authApi.get(`/api/profile/${userId}`);
  } catch (error) {
    console.error(`Error al obtener el perfil del usuario ${userId}:`, error);
    
    // Fallback con datos mock solo en caso de error
    console.log(`Usando datos mock como fallback para el usuario ${userId}`);
    return {
      success: true,
      data: {
        firstName: 'Usuario',
        lastName: 'Ejemplo',
        username: `usuario${userId}`,
        biography: 'Esta es una biografía de ejemplo para un perfil de usuario.',
        email: `usuario${userId}@example.com`,
        dateBirth: '1990-01-01',
        interests: ['Programación', 'JavaScript', 'React'],
        following: 42,
        followers: 120,
        groups: 5,
        contentCount: 18,
        helpRequestCount: 3,
        isFollowing: false
      }
    };
  }
};

// Get follow status with a user
export const getFollowStatus = async (userId) => {
  try {
    console.log(`Obteniendo estado de seguimiento para el usuario ${userId}`);
    // En lugar de un endpoint separado, usamos la información del perfil que ya incluye isFollowing
    const profileResponse = await authApi.get(`/api/profile/${userId}`);
    if (profileResponse.success) {
      return {
        success: true,
        data: {
          isFollowing: profileResponse.data.isFollowing || false
        }
      };
    } else {
      throw new Error('No se pudo obtener el perfil del usuario');
    }
  } catch (error) {
    console.error(`Error al obtener el estado de seguimiento para el usuario ${userId}:`, error);
    
    // Fallback con datos mock
    console.log(`Usando datos mock para el estado de seguimiento del usuario ${userId}`);
    return {
      success: true,
      data: {
        isFollowing: Math.random() > 0.5 // 50% probabilidad de que sea true
      }
    };
  }
};

// Follow a user
export const followUser = async (userId) => {
  // Endpoint real: POST /api/user/{userId}/follow
  // Como el backend aún no tiene implementada esta ruta, usaremos datos mock
  
  console.log(`Siguiendo al usuario ${userId} (modo mock)`);
  return {
    success: true,
    message: `Ahora estás siguiendo al usuario ${userId}`
  };
  
  /* Descomentar cuando el backend implemente esta ruta
  try {
    return await authApi.post(`/api/user/${userId}/follow`);
  } catch (error) {
    console.error(`Error al seguir al usuario ${userId}:`, error);
    
    // Fallback con datos mock
    return {
      success: true,
      message: `Ahora estás siguiendo al usuario ${userId}`
    };
  }
  */
};

// Unfollow a user
export const unfollowUser = async (userId) => {
  // Endpoint real: POST /api/user/{userId}/unfollow
  // o también podría ser: DELETE /api/user/{userId}/follow
  // Como el backend aún no tiene implementada esta ruta, usaremos datos mock
  
  console.log(`Dejando de seguir al usuario ${userId} (modo mock)`);
  return {
    success: true,
    message: `Has dejado de seguir al usuario ${userId}`
  };

  /* Descomentar cuando el backend implemente esta ruta  
  try {
    return await authApi.post(`/api/user/${userId}/unfollow`);
  } catch (error) {
    console.error(`Error al dejar de seguir al usuario ${userId}:`, error);
    
    // Fallback con datos mock
    return {
      success: true,
      message: `Has dejado de seguir al usuario ${userId}`
    };
  }
  */
};

/**
 * Obtener lista de seguidores del usuario actual
 */
export const getFollowers = async () => {
  try {
    return await authApi.get('/api/profile/followers');
  } catch (error) {
    console.error('Error en getFollowers:', error);
    return { 
      success: false, 
      message: error.message || 'Error al obtener seguidores' 
    };
  }
};

/**
 * Obtener lista de usuarios seguidos por el usuario actual
 */
export const getFollowing = async () => {
  try {
    return await authApi.get('/api/profile/following');
  } catch (error) {
    console.error('Error en getFollowing:', error);
    return { 
      success: false, 
      message: error.message || 'Error al obtener seguidos' 
    };
  }
};

export default {
  getProfile,
  updateProfile,
  getProfileByUserId,
  getFollowStatus,
  followUser,
  unfollowUser,
  getFollowers,
  getFollowing
};