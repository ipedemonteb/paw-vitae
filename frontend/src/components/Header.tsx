import { Button } from "@/components/ui/button"
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from "@/components/ui/dropdown-menu"
import { ChevronDown, User, BriefcaseMedical } from "lucide-react";

const header =
    "fixed top-0 left-0 w-full shadow-[var(--shadow-md)] py-[30px] leading-[1.6] bg-white";
const headerContainer =
    "flex items-center justify-between w-full max-w-[1200px] px-[20px] mx-auto";
const logo =
    "flex items-center text-[var(--primary-color)] transition-transform duration-300 ease-[ease] hover:scale-105";
const btnBase =
    "inline-flex items-center justify-center text-[16px] font-medium leading-[1.5] px-[24px] h-[44px] rounded-md !border-2 transition-colors transition-transform duration-300 ease-in-out";
const btnOutline =
    `${btnBase} bg-transparent text-[var(--primary-color)] border-[var(--primary-color)] hover:bg-[var(--primary-color)] hover:text-[var(--white)]`;
const btnFilled =
    `${btnBase} bg-[var(--primary-color)] text-[var(--white)] border-[var(--primary-color)] hover:bg-transparent hover:text-[var(--primary-color)]`;
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
                    <div>
                        Hello
                    </div>
                    <div className="flex gap-[12px] items-center">
                        <Button className={btnOutline}>Login</Button>
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