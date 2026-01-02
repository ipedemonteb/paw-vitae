import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from "@/components/ui/dropdown-menu"
import { NavigationMenu, NavigationMenuList, NavigationMenuItem, NavigationMenuLink } from "@/components/ui/navigation-menu"
import { ChevronDown, User, BriefcaseMedical } from "lucide-react";
import { Link, useMatch } from "react-router-dom";
import { cn } from "@/lib/utils";
import React from "react";

function HeaderNavLink({ to, end, children, }: {
    to: string;
    end?: boolean;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <NavigationMenuLink asChild className={cn(navItem, match && navItemActive)}>
            <Link to={to}>{children}</Link>
        </NavigationMenuLink>
    );
}

const header =
    "fixed top-0 left-0 w-full shadow-[var(--shadow-md)] py-[30px] leading-[1.6] bg-white z-50";
const headerContainer =
    "flex items-center justify-between w-full max-w-[1200px] px-[20px] mx-auto";
const logo =
    "flex items-center text-[var(--primary-color)] transition-transform duration-300 ease-[ease] hover:scale-105";
const nav =
    "font-medium";
const navItem =
    "text-base cursor-pointer hover:text-[var(--primary-color)] hover:bg-transparent " +
    "relative px-0 py-[5px] font-medium transition-colors duration-300 " +
    "focus:bg-transparent active:bg-transparent focus:text-[var(--primary-color)] " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-[2px] after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const navItemActive =
    "text-[var(--primary-color)] font-semibold after:w-full";
const btnBase =
    "inline-flex items-center justify-center text-base font-medium leading-[1.5] px-[24px] h-[44px] rounded-md !border-2 transition-colors transition-transform duration-300 ease-in-out";
const btnOutline =
    `${btnBase} bg-transparent text-[var(--primary-color)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] hover:text-[var(--white)]`;
const btnFilled =
    `${btnBase} bg-[var(--primary-color)] text-[var(--white)] border-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer`;
const dropDownItem =
    "flex items-center gap-2 text-[16px] cursor-pointer data-[highlighted]:text-[var(--primary-color)] data-[highlighted]:bg-transparent";

function Header() {
    return (
        <>
            <div className={header}>
                <div className={headerContainer}>
                    <a href="/" className={logo}>
                        <h1 className="block text-5xl font-bold no-underline">Vitae</h1>
                    </a>
                    <NavigationMenu className={nav}>
                        <NavigationMenuList className="flex gap-[30px] items-center">
                            <NavigationMenuItem>
                                <HeaderNavLink to="/" end>
                                    Home
                                </HeaderNavLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                                <HeaderNavLink to="/search">
                                    Find Doctors
                                </HeaderNavLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                                <HeaderNavLink to="/about-us">
                                    About Us
                                </HeaderNavLink>
                            </NavigationMenuItem>
                        </NavigationMenuList>
                    </NavigationMenu>
                    <div className="flex gap-[12px] items-center">
                        <Link to="/login" type="button" className={btnOutline}>
                            Login
                        </Link>
                        <DropdownMenu>
                            <DropdownMenuTrigger className={btnFilled}>
                                Register
                                <ChevronDown className="h-4 w-4"/>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent>
                                <DropdownMenuItem className={dropDownItem}>
                                    <User className="text-inherit"/>
                                    Register
                                </DropdownMenuItem>
                                <DropdownMenuItem className={dropDownItem}>
                                    <BriefcaseMedical className="text-inherit"/>
                                    Are you a Doctor?
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Header