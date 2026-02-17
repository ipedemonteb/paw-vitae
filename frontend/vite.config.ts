/// <reference types="vitest/config" />

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from "@tailwindcss/vite"
import { fileURLToPath, URL } from "node:url";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {

    const isProduction = mode === 'production';

    return {
        base: isProduction ? "/paw-2025a-11/" : "/",

        root: '.',
        plugins: [
            react(),
            tailwindcss(),
        ],
        resolve: {
            alias: {
                "@": fileURLToPath(new URL("./src", import.meta.url)),
            },
            dedupe: ['react', 'react-dom'],
        },
        test: {
            globals: true,
            environment: 'jsdom',
            setupFiles: './src/__test__/setup/setup.ts',
            testTimeout: 15000,
            hookTimeout: 15000,
            css: true
        }
    };
})