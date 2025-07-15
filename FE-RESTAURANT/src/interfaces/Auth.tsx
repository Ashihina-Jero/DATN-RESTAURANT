export interface Auth {
  token: string;
  tokenType?: string; 
  username: string;
}

export function createAuthResponse(token: string, username: string): Auth{
  return {
    token,
    username,
    tokenType: 'Bearer',
  };
}
