import Constants from "expo-constants";
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

type ExpoExtras = {
  firebaseApiKey?: string;
  firebaseAuthDomain?: string;
  firebaseProjectId?: string;
  firebaseStorageBucket?: string;
  firebaseMessagingSenderId?: string;
  firebaseAppId?: string;
};

const extras = (Constants.expoConfig?.extra ?? {}) as ExpoExtras;

function requiredEnv(name: keyof ExpoExtras) {
  const value = extras[name];
  if (!value) {
    throw new Error(`Missing Expo extra "${name}". Check app.config.ts and .env.`);
  }
  return value;
}

const firebaseConfig = {
  apiKey: requiredEnv("firebaseApiKey"),
  authDomain: requiredEnv("firebaseAuthDomain"),
  projectId: requiredEnv("firebaseProjectId"),
  storageBucket: requiredEnv("firebaseStorageBucket"),
  messagingSenderId: requiredEnv("firebaseMessagingSenderId"),
  appId: requiredEnv("firebaseAppId")
};

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);
export const db = getFirestore(app);
