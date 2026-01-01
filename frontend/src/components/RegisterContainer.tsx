import React from "react";

interface RegisterContainerProps {
    children: React.ReactNode;
}

function RegisterContainer({ children }: RegisterContainerProps) {
    return (
        <div className={`
      flex flex-col
      md:flex-row
      w-full max-w-[600px]
      min-h-[600px]
      bg-white
      overflow-hidden
      relative
      
      
      min-[900px]:flex-row
      min-[900px]:max-w-[1200px]
      min-[900px]:rounded-[20px]
      min-[900px]:shadow-[var(--shadow-lg)]
      

      rounded-md shadow-none
    `}>
            {children}
        </div>
    );
}

export default RegisterContainer;